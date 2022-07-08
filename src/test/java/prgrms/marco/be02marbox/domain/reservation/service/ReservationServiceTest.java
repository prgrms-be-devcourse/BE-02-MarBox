package prgrms.marco.be02marbox.domain.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.repository.RepositoryTestUtil;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;
import prgrms.marco.be02marbox.domain.theater.service.SeatService;
import prgrms.marco.be02marbox.domain.theater.service.utils.ScheduleConverter;

@Import({ReservationService.class,
	ReservedSeatService.class,
	SeatService.class,
	ScheduleService.class,
	ScheduleConverter.class,
	MovieConverter.class
})
class ReservationServiceTest extends RepositoryTestUtil {
	@Autowired
	ReservationService reservationService;

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
}
