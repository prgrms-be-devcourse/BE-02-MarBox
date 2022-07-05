package prgrms.marco.be02marbox.domain.theater.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.repository.RepositoryTestUtil;
import prgrms.marco.be02marbox.domain.reservation.service.ReservedSeatService;
import prgrms.marco.be02marbox.domain.theater.Schedule;

@Import({ReservedSeatService.class, SeatService.class})
class SeatServiceTest extends RepositoryTestUtil {

	@Autowired
	ReservedSeatService reservedSeatService;

	@Autowired
	SeatService seatService;

	@Test
	@DisplayName("예약 좌석 정보를 조회한다.")
	void testFindReservedSeat() {
		int totalSeatCount = 5;
		int reservedCount = 3;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, reservedCount);

		Set<Long> reservePossibleSeats = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		List<ResponseFindReservedSeat> reservedSeat = seatService.findAvailableSeatList(
			schedule.getTheaterRoom().getId(),
			reservePossibleSeats);

		long resultReservedCount = reservedSeat.stream()
			.filter(responseFindReservedSeat -> responseFindReservedSeat.reserved())
			.count();

		assertAll(
			() -> assertThat(reservedSeat).hasSize(totalSeatCount),
			() -> assertThat(reservedCount).isEqualTo(resultReservedCount)
		);
	}
}
