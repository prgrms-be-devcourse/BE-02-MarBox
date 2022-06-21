package prgrms.marco.be02marbox.domain.theater.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;

@DataJpaTest
class TheaterRoomRepositoryTest {

	@Autowired
	private TheaterRoomRepository theaterRoomRepository;

	@Autowired
	private TheaterRepository theaterRepository;

	@Autowired
	private SeatRepository seatRepository;

	private final Theater theater = new Theater();
	private TheaterRoom theaterRoom;
	private List<Seat> seats = new ArrayList<>();

	@BeforeEach
	void init() {
		theaterRepository.save(theater);
		seats.clear();
		seats.add(new Seat(0, 0));
		seats.add(new Seat(0, 1));
		seats.add(new Seat(0, 2));
		theaterRoom = new TheaterRoom(theater, "A관", seats);
		theaterRoomRepository.save(theaterRoom);
	}

	@AfterEach
	void clear() {
		theaterRepository.deleteAll();
		theaterRoomRepository.deleteAll();
	}

	@Test
	@DisplayName("TheaterRoom cascade 저장 성공")
	void testSave() {
		List<Seat> findSeats = seatRepository.findAll();
		Optional<TheaterRoom> findTheaterRoom = theaterRoomRepository.findById(theaterRoom.getId());
		assertAll(
			() -> assertThat(findTheaterRoom).isPresent(),
			() -> {
				TheaterRoom getTheaterRoom = findTheaterRoom.get();
				assertThat(getTheaterRoom).isEqualTo(theaterRoom);
				assertThat(getTheaterRoom.getSeats()).hasSize(seats.size());
				assertThat(getTheaterRoom.getTotalSeats()).isEqualTo(seats.size());
			},
			() -> assertThat(findSeats).hasSize(seats.size()),
			() -> assertThat(findSeats.get(0).getTheaterRoom().getId()).isEqualTo(theaterRoom.getId())
		);
	}

	@Test
	@DisplayName("TheaterRoom cascade 삭제 성공")
	void testDelete() {
		theaterRoomRepository.deleteById(theaterRoom.getId());

		List<Seat> findSeats = seatRepository.findAll();
		Optional<TheaterRoom> findTheaterRoom = theaterRoomRepository.findById(theaterRoom.getId());

		assertAll(
			() -> assertThat(findSeats).isEmpty(),
			() -> assertThat(findTheaterRoom).isEmpty()
		);
	}

	@Test
	@DisplayName("orphanRemoval=true 적용 성공")
	void testOrphanRemovalTrue() {
		TheaterRoom findTheaterRoom = theaterRoomRepository.findById(theaterRoom.getId()).get();

		int beforeSize = findTheaterRoom.getTotalSeats();
		findTheaterRoom.getSeats().remove(0);

		List<Seat> findSeats = seatRepository.findAll();
		assertThat(findSeats).hasSize(beforeSize - 1);
	}
}
