package prgrms.marco.be02marbox.domain.reservation.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static prgrms.marco.be02marbox.domain.reservation.ReservedSeat.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.theater.Schedule;

class ReservedSeatRepositoryTest extends RepositoryTestUtil {
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

		List<ReservedSeat> reservedSeats = reservedSeatRepository.searchByScheduleIdStartsWith(schedule.getId());
		assertThat(reservedSeats).hasSize(expectCount);
	}

	@Test
	@DisplayName("저장되지 않은 {schedule_id}) 로 조회하는 경우")
	void testSearchByIdStartsWithBadParam2() {
		Schedule schedule = saveReservedSeatMultiSeat(1);
		List<ReservedSeat> reservedSeats = reservedSeatRepository.searchByScheduleIdStartsWith(schedule.getId() + 1);

		assertThat(reservedSeats).isEmpty();
	}
}
