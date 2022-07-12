package prgrms.marco.be02marbox.domain.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;
import prgrms.marco.be02marbox.domain.reservation.Account;
import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.RequestCreateTicket;
import prgrms.marco.be02marbox.domain.reservation.dto.RequestReservation;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.repository.AccountRepository;
import prgrms.marco.be02marbox.domain.reservation.repository.RepositoryTestUtil;
import prgrms.marco.be02marbox.domain.reservation.service.utils.TicketConverter;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;
import prgrms.marco.be02marbox.domain.theater.service.SeatService;
import prgrms.marco.be02marbox.domain.theater.service.utils.ScheduleConverter;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@Import({ReservationService.class,
	ReservedSeatService.class,
	SeatService.class,
	ScheduleService.class,
	ScheduleConverter.class,
	MovieConverter.class,
	AccountService.class,
	TicketService.class,
	TicketConverter.class,
	UserService.class
})
class ReservationServiceTest extends RepositoryTestUtil {
	@Autowired
	ReservationService reservationService;

	@Autowired
	AccountRepository accountRepository;

	@MockBean
	PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("존재하지 않는 스케줄 id로 예매 가능 좌석 조회시 IllegalArgumentException")
	void testFindReservePossibleSeatsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> reservationService.findReservePossibleSeatList(-1L));
	}

	@Test
	@DisplayName("예매 가능한 좌석 조회")
	void testFindReservePossibleSeats() {
		int totalSeatCount = 5;
		int reservedCount = 2;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, reservedCount);

		List<ResponseFindReservedSeat> reservePossibleSeats = reservationService.findReservePossibleSeatList(
			schedule.getId());
		assertThat(reservePossibleSeats).hasSize(totalSeatCount);
	}

	@Test
	@DisplayName("예매 가능한 좌석 없는 경우")
	void testFindReservePossibleSeatsEmpty() {
		Schedule schedule = saveSchedule();

		List<ResponseFindReservedSeat> reservePossibleSeats = reservationService.findReservePossibleSeatList(
			schedule.getId());
		assertThat(reservePossibleSeats).isEmpty();
	}

	@Test
	@DisplayName("예매된 좌석이 하나도 없는 경우")
	void testFindReservePossibleSeatsAllReservePossible() {
		int totalSeatCount = 3;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, 0);

		List<ResponseFindReservedSeat> reservePossibleSeats = reservationService.findReservePossibleSeatList(
			schedule.getId());
		assertThat(reservePossibleSeats).hasSize(totalSeatCount);
	}

	@Test
	@DisplayName("예약 성공")
	void testReservation() {
		// given
		User user = saveUser();
		Schedule schedule = saveSchedule();

		int totalSeatCount = 5;
		TheaterRoom theaterRoom = saveSeatMulti(totalSeatCount);
		List<Seat> seats = seatRepository.findByTheaterRoomId(theaterRoom.getId());
		List<Long> seatIdList = seats.stream().map(Seat::getId).toList();

		Account account = new Account(user, 50000);
		Account saveAccount = accountRepository.save(account);

		RequestReservation request = new RequestReservation(user.getId(), schedule.getId(), seatIdList);

		// when
		Long ticketId = reservationService.reservation(request);

		//then
		em.flush();
		em.clear();

		Ticket findTicket = ticketRepository.findById(ticketId).get();
		List<ReservedSeat> reservedSeats = reservedSeatRepository.searchByScheduleIdStartsWith(schedule.getId());

		assertThat(findTicket).isNotNull();
		assertThat(saveAccount.getMoney()).isEqualTo(0);
		assertThat(reservedSeats).hasSize(seats.size());
	}


}
