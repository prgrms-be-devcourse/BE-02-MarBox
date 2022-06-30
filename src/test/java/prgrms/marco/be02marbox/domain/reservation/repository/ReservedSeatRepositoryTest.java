package prgrms.marco.be02marbox.domain.reservation.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static prgrms.marco.be02marbox.domain.reservation.ReservedSeat.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.theater.Schedule;

class ReservedSeatRepositoryTest extends RepositoryTestUtil {
	@AfterEach
	void init() {
		queryCall();
	}

	@Test
	@DisplayName("scheduleId와 seatId로 아이디를 생성한다.")
	void testMakeReservedSeatId() {
		ReservedSeat reservedSeat = saveReservedSeat();

		Optional<ReservedSeat> findReservedSeat = reservedSeatRepository.findById(reservedSeat.getId());
		assertAll(
			() -> assertThat(findReservedSeat).isPresent(),
			() -> {
				ReservedSeat getReservedSeat = findReservedSeat.get();
				StringBuilder makeId = new StringBuilder()
					.append(reservedSeat.getTicket().getSchedule().getId())
					.append(ID_SEPARATOR)
					.append(reservedSeat.getSeat().getId());
				assertThat(getReservedSeat.getId()).isEqualTo(makeId.toString());
			}
		);
	}

	@ParameterizedTest
	@CsvSource({"13", "5"})
	@DisplayName("like 조건을 통해 스케줄 id 에 예메된 좌석정보를 조회한다.")
	void testSearchByIdStartsWith(int expectCount) {
		Schedule schedule = saveReservedSeatMultiSeat(expectCount);

		String paramId = makeFindByScheduleParam(schedule.getId());
		List<ReservedSeat> reservedSeats = reservedSeatRepository.searchByScheduleIdStartsWith(paramId);

		assertThat(reservedSeats).hasSize(expectCount);
	}

	@Test
	@DisplayName("like 조건 잘못된 id형식으로 조회하는 경우")
	void testSearchByIdStartsWithBadParam() {
		Schedule schedule = saveReservedSeatMultiSeat(1);
		String paramId = new StringBuilder().append(schedule.getId()).append("\\_\\_").toString();
		String paramId2 = new StringBuilder().append(schedule.getId()).append("*").toString();
		String paramId3 = new StringBuilder().append(schedule.getId()).append("?").toString();

		List<ReservedSeat> reservedSeats = reservedSeatRepository.searchByScheduleIdStartsWith(paramId);
		List<ReservedSeat> reservedSeats2 = reservedSeatRepository.searchByScheduleIdStartsWith(paramId2);
		List<ReservedSeat> reservedSeats3 = reservedSeatRepository.searchByScheduleIdStartsWith(paramId3);

		assertAll(
			() -> assertThat(reservedSeats).isEmpty(),
			() -> assertThat(reservedSeats2).isEmpty(),
			() -> assertThat(reservedSeats3).isEmpty()
		);
	}

	@Test
	@DisplayName("저장되지 않은 {schedule_id})_ 로 조회하는 경우")
	void testSearchByIdStartsWithBadParam2() {
		Schedule schedule = saveReservedSeatMultiSeat(3);
		String paramId = makeFindByScheduleParam(schedule.getId() + 1);
		List<ReservedSeat> reservedSeats = reservedSeatRepository.searchByScheduleIdStartsWith(paramId);

		assertThat(reservedSeats).isEmpty();
	}

	private String makeFindByScheduleParam(Long scheduleId) {
		return new StringBuilder().append(scheduleId).append(ID_SEPARATOR).toString();
	}
}
