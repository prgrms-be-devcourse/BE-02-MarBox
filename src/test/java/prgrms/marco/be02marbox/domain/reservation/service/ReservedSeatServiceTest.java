package prgrms.marco.be02marbox.domain.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.repository.RepositoryTestUtil;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;
import prgrms.marco.be02marbox.domain.theater.service.utils.SeatConverter;

@Import({ReservedSeatService.class, SeatConverter.class})
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
	@DisplayName("예매 좌석 조회")
	void testFindReservePossibleSeats() {
		int totalSeatCount = 5;
		int reservedCount = 2;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, reservedCount);

		List<Long> reservePossibleSeats = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		assertThat(reservePossibleSeats).hasSize(reservedCount);
	}

	@Test
	@DisplayName("예매 좌석 없는 경우")
	void testFindReservePossibleSeatsEmpty() {
		Schedule schedule = saveSchedule();

		List<Long> reservePossibleSeats = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		assertThat(reservePossibleSeats).isEmpty();
	}

	@Test
	@DisplayName("모든 좌석이 예매 된 경우")
	void testFindReservePossibleSeatsAllReserved() {
		int totalSeatCount = 2;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, totalSeatCount);

		List<Long> reservePossibleSeats = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		assertThat(reservePossibleSeats).hasSize(totalSeatCount);
	}
}
