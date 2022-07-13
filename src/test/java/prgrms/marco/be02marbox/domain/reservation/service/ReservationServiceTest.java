package prgrms.marco.be02marbox.domain.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import prgrms.marco.be02marbox.domain.reservation.Account;
import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.RequestReservation;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.repository.AccountRepository;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.user.User;

class ReservationServiceTest extends ServiceTestUtil {

	private static final int PAY_AMOUNT = 10000;

	@Autowired
	ReservationService reservationService;

	@Autowired
	AccountRepository accountRepository;

	@AfterEach
	void clear() {
		reservedSeatRepository.deleteAllInBatch();
		ticketRepository.deleteAllInBatch();
		seatRepository.deleteAllInBatch();
		accountRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();

		scheduleRepository.deleteAllInBatch();
		movieRepository.deleteAllInBatch();

		theaterRoomRepository.deleteAllInBatch();
		theaterRepository.deleteAllInBatch();
	}

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

		int totalSeatCount = 2;
		TheaterRoom theaterRoom = saveSeatMulti(totalSeatCount);
		List<Seat> seats = seatRepository.findByTheaterRoomId(theaterRoom.getId());
		List<Long> seatIdList = seats.stream().map(Seat::getId).toList();

		Account account = new Account(user, totalSeatCount * PAY_AMOUNT);
		Account saveAccount = accountRepository.save(account);

		RequestReservation request = new RequestReservation(user.getId(), schedule.getId(), seatIdList);

		// when
		Long ticketId = reservationService.reservation(request);

		// then
		Ticket findTicket = ticketRepository.findById(ticketId).get();
		List<ReservedSeat> reservedSeats = reservedSeatRepository.searchByScheduleIdStartsWith(schedule.getId());
		Account findAccount = accountRepository.findById(saveAccount.getId()).get();
		assertThat(findTicket).isNotNull();
		assertThat(findAccount.getMoney()).isZero();
		assertThat(reservedSeats).hasSize(seats.size());
	}

	@Test
	@DisplayName("예약 실패 - 잔액부족")
	void testReservationNoMoney() {
		// given
		User user = saveUser();
		Schedule schedule = saveSchedule();

		int totalSeatCount = 1;
		TheaterRoom theaterRoom = saveSeatMulti(totalSeatCount);
		List<Seat> seats = seatRepository.findByTheaterRoomId(theaterRoom.getId());
		List<Long> seatIdList = seats.stream().map(Seat::getId).toList();

		Integer userMoney = totalSeatCount * PAY_AMOUNT - 1;
		Account account = new Account(user, userMoney);
		Account saveAccount = accountRepository.save(account);

		RequestReservation request = new RequestReservation(user.getId(), schedule.getId(), seatIdList);

		// then
		assertAll(
			() -> assertThatThrownBy(() -> reservationService.reservation(request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining(NO_MONEY_EXP_MSG.getMessage()),
			() -> assertThat(saveAccount.getMoney()).isEqualTo(userMoney)
		);
	}

	@Test
	@DisplayName("예약 실패 - saveAll 실패 시 RollBack 확인")
	void testReservationSaveAllExp() {
		// given
		User user = saveUser();
		Schedule schedule = saveSchedule();

		int totalSeatCount = 1;
		TheaterRoom theaterRoom = saveSeatMulti(totalSeatCount);
		List<Seat> seats = seatRepository.findByTheaterRoomId(theaterRoom.getId());
		List<Long> seatIdList = seats.stream().map(Seat::getId).toList();

		Integer userMoney = totalSeatCount * PAY_AMOUNT * 3;
		Account account = new Account(user, userMoney);
		Account saveAccount = accountRepository.save(account);
		RequestReservation request = new RequestReservation(user.getId(), schedule.getId(), seatIdList);
		reservationService.reservation(request);

		Account findAccount = accountRepository.findById(saveAccount.getId()).get();
		long ticketCount = ticketRepository.count();
		int size = reservedSeatRepository.searchByScheduleIdStartsWith(schedule.getId()).size();

		// then
		assertThatThrownBy(() -> reservationService.reservation(request))
			.isInstanceOf(DataIntegrityViolationException.class);

		Account findAccount2 = accountRepository.findById(saveAccount.getId()).get();
		long ticketCount2 = ticketRepository.count();

		int size2 = reservedSeatRepository.searchByScheduleIdStartsWith(schedule.getId()).size();

		assertThat(findAccount2.getMoney()).isEqualTo(findAccount.getMoney());
		assertThat(ticketCount2).isEqualTo(ticketCount);
		assertThat(size2).isEqualTo(size);
	}
}
