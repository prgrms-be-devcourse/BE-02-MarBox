package prgrms.marco.be02marbox.domain.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.repository.RepositoryTestUtil;
import prgrms.marco.be02marbox.domain.theater.Schedule;

@Import({ReservedSeatService.class})
class ReservedSeatServiceTest extends RepositoryTestUtil {
	@Autowired
	ReservedSeatService reservedSeatService;

	@Test
	@DisplayName("스케줄 id로 좌석정보를 조회할 수 있다.")
	void testFindReservedIdListByScheduleId() {
		int seatCount = 3;
		Schedule schedule = saveReservedSeatMultiSeat(seatCount);

		Set<Long> seatList = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		assertThat(seatList).hasSize(seatCount);
	}

	@Test
	@DisplayName("스케줄 id로 좌석정보를 조회할 수 있다. - 여러 스케줄이 저장되어 있는 경우")
	void testFindReservedIdListByScheduleIdMultiSchedule() {
		int cnt = 1;
		int cnt2 = 2;
		int cnt3 = 3;

		Schedule schedule = saveReservedSeatMultiSeat(cnt);
		Schedule schedule2 = saveReservedSeatMultiSeat(cnt2);
		Schedule schedule3 = saveReservedSeatMultiSeat(cnt3);

		List<ReservedSeat> all = reservedSeatRepository.findAll();

		Set<Long> seatList = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		Set<Long> seatList2 = reservedSeatService.findReservedIdListByScheduleId(schedule2.getId());
		Set<Long> seatList3 = reservedSeatService.findReservedIdListByScheduleId(schedule3.getId());

		assertAll(
			() -> assertThat(all).hasSize(cnt + cnt2 + cnt3),
			() -> assertThat(seatList).hasSize(cnt),
			() -> assertThat(seatList2).hasSize(cnt2),
			() -> assertThat(seatList3).hasSize(cnt3)
		);
	}

	@Test
	@DisplayName("저장되지 않은 scheduleId로 조회하는 경우")
	void testFindReservedIdListByScheduleIdWrongScheduleId() {
		saveReservedSeat();

		Set<Long> seatList = reservedSeatService.findReservedIdListByScheduleId(-1L);
		assertThat(seatList).isEmpty();
	}

	@Test
	@DisplayName("예매 좌석이 존재하는 경우")
	void testFindReservedIdListByScheduleIdExist() {
		int totalSeatCount = 5;
		int reservedCount = 2;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, reservedCount);

		Set<Long> reservePossibleSeats = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		assertThat(reservePossibleSeats).hasSize(reservedCount);
	}

	@Test
	@DisplayName("예매 좌석 없는 경우")
	void testFindReservedIdListByScheduleIdEmpty() {
		Schedule schedule = saveSchedule();

		Set<Long> reservePossibleSeats = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		assertThat(reservePossibleSeats).isEmpty();
	}

	@Test
	@DisplayName("모든 좌석이 예매 된 경우")
	void testFindReservedIdListByScheduleIdAllReserved() {
		int totalSeatCount = 2;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, totalSeatCount);

		Set<Long> reservePossibleSeats = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		assertThat(reservePossibleSeats).hasSize(totalSeatCount);
	}
}
