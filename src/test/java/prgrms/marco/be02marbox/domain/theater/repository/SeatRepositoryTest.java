package prgrms.marco.be02marbox.domain.theater.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.config.QueryDslConfig;
import prgrms.marco.be02marbox.domain.reservation.repository.RepositoryTestUtil;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;


class SeatRepositoryTest extends RepositoryTestUtil {

	@Test
	@DisplayName("findByRoomId 테스트")
	void testFindByTheaterRoomId() {
		TheaterRoom theaterRoom = saveTheaterRoom("A관");
		TheaterRoom theaterRoom2 = saveTheaterRoom("B관");
		saveSeat(theaterRoom, 0, 0);
		saveSeat(theaterRoom, 0, 1);
		saveSeat(theaterRoom2, 0, 0);

		List<Seat> seatList = seatRepository.findByTheaterRoomId(theaterRoom.getId());
		List<Seat> seatList2 = seatRepository.findByTheaterRoomId(theaterRoom2.getId());

		assertAll(
			() -> assertThat(seatList).hasSize(2),
			() -> assertThat(seatList2).hasSize(1)
		);
	}

	@Test
	@DisplayName("예매 가능한 좌석을 조회할 수 있다.")
	void testFindByTheaterRoomIdAndIdNotIn() {
		int totalSeatCount = 5;
		int reservedCount = 2;
		Schedule schedule = saveSeatAndReserveSeat(totalSeatCount, reservedCount);

		List<Long> reservedSeatIdList = new ArrayList<>();
		reservedSeatRepository.searchByScheduleIdStartsWith(schedule.getId())
			.forEach(reservedSeat -> reservedSeatIdList.add(reservedSeat.getSeat().getId()));

		List<Seat> reservePossibleSeats = seatRepository.findByTheaterRoomIdAndIdNotIn(
			schedule.getTheaterRoom().getId(), reservedSeatIdList);
		assertThat(reservePossibleSeats).hasSize(totalSeatCount - reservedCount);
	}

	@Test
	@DisplayName("인자로 전달한 Id가 DB에 존재하는 만큼 찾아온다")
	void testFindByIdIn() {
		int totalSeatCount = 5;

		TheaterRoom theaterRoom = saveSeatMulti(totalSeatCount);

		List<Seat> seats = seatRepository.findByTheaterRoomId(theaterRoom.getId());
		List<Long> seatIdList = seats.stream()
			.map(Seat::getId).toList();

		List<Long> oddIds = seats.stream()
			.map(Seat::getId)
			.filter(id -> id % 2 == 1)
			.toList();
		List<Seat> savedSeats = seatRepository.findByIdIn(seatIdList);
		List<Seat> findOdds = seatRepository.findByIdIn(oddIds);

		assertAll(
			() -> assertThat(savedSeats).hasSize(totalSeatCount),
			() -> assertThat(findOdds).hasSize(oddIds.size())
		);
	}

}
