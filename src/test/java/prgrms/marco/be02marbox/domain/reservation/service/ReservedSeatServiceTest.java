package prgrms.marco.be02marbox.domain.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;
import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.repository.RepositoryTestUtil;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;
import prgrms.marco.be02marbox.domain.theater.service.SeatService;
import prgrms.marco.be02marbox.domain.theater.service.utils.ScheduleConverter;
import prgrms.marco.be02marbox.domain.theater.service.utils.SeatConverter;

@Import({ReservedSeatService.class, SeatConverter.class, SeatService.class, ScheduleService.class,
	ScheduleConverter.class, MovieConverter.class})
class ReservedSeatServiceTest extends RepositoryTestUtil {
	@Autowired
	ReservedSeatService reservedSeatService;

	@Test
	@DisplayName("스케줄 id로 좌석정보를 조회할 수 있다.")
	void testFindByScheduleId() {
		int seatCount = 3;
		Schedule schedule = saveReservedSeatMultiSeat(seatCount);

		List<ResponseFindSeat> seatList = reservedSeatService.findByScheduleId(schedule.getId());
		assertThat(seatList).hasSize(seatCount);
	}

	@Test
	@DisplayName("스케줄 id로 좌석정보를 조회할 수 있다. - 여러 스케줄이 저장되어 있는 경우")
	void testFindByScheduleId_multiSchedule() {
		int cnt = 1;
		int cnt2 = 2;
		int cnt3 = 3;

		Schedule schedule = saveReservedSeatMultiSeat(cnt);
		Schedule schedule2 = saveReservedSeatMultiSeat(cnt2);
		Schedule schedule3 = saveReservedSeatMultiSeat(cnt3);

		List<ReservedSeat> all = reservedSeatRepository.findAll();

		List<ResponseFindSeat> seatList = reservedSeatService.findByScheduleId(schedule.getId());
		List<ResponseFindSeat> seatList2 = reservedSeatService.findByScheduleId(schedule2.getId());
		List<ResponseFindSeat> seatList3 = reservedSeatService.findByScheduleId(schedule3.getId());

		assertAll(
			() -> assertThat(all).hasSize(cnt + cnt2 + cnt3),
			() -> assertThat(seatList).hasSize(cnt),
			() -> assertThat(seatList2).hasSize(cnt2),
			() -> assertThat(seatList3).hasSize(cnt3)
		);
	}

	@Test
	@DisplayName("저장되지 않은 scheduleId로 조회하는 경우")
	void testFindByScheduleId_wrongScheduleId() {
		saveReservedSeat();

		List<ResponseFindSeat> seatList = reservedSeatService.findByScheduleId(-1L);
		assertThat(seatList).isEmpty();
	}

	@Test
	@DisplayName("존재하지 않는 스케줄 id로 예매 가능 좌석 조회시 IllegalArgumentException")
	void testFindReservePossibleSeatsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> reservedSeatService.findReservePossibleSeats(-1L));
	}

	@Test
	@DisplayName("예매 가능한 좌석 조회")
	void testFindReservePossibleSeats() {
		int totalSeatCount = 5;
		int reservedCount = 2;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, reservedCount);

		List<ResponseFindSeat> reservePossibleSeats = reservedSeatService.findReservePossibleSeats(schedule.getId());
		assertThat(reservePossibleSeats).hasSize(totalSeatCount - reservedCount);
	}

	@Test
	@DisplayName("예매 가능한 좌석 없는 경우")
	void testFindReservePossibleSeatsEmpty() {
		Schedule schedule = saveSchedule();

		List<ResponseFindSeat> reservePossibleSeats = reservedSeatService.findReservePossibleSeats(schedule.getId());
		assertThat(reservePossibleSeats).isEmpty();
	}

	@Test
	@DisplayName("모든 좌석이 예매 된 경우 - 비어있는 리스트 반환")
	void testFindReservePossibleSeatsAllReserved() {
		Schedule schedule = saveSeatAndReserveSeat(2, 2);

		List<ResponseFindSeat> reservePossibleSeats = reservedSeatService.findReservePossibleSeats(schedule.getId());
		assertThat(reservePossibleSeats).isEmpty();
	}

	@Test
	@DisplayName("예매된 좌석이 하나도 없는 경우")
	void testFindReservePossibleSeatsAllReservePossible() {
		int totalSeatCount = 3;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, 0);

		List<ResponseFindSeat> reservePossibleSeats = reservedSeatService.findReservePossibleSeats(schedule.getId());
		assertThat(reservePossibleSeats).hasSize(totalSeatCount);
	}
}
