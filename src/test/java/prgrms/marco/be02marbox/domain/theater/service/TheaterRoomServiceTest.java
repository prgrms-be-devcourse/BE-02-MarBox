package prgrms.marco.be02marbox.domain.theater.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSeat;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.repository.SeatRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.SeatConverter;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterConverter;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterRoomConverter;

@DataJpaTest
@Import({TheaterRoomService.class, SeatConverter.class, TheaterRoomConverter.class, TheaterConverter.class})
class TheaterRoomServiceTest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TheaterRoomService theaterRoomService;
	@Autowired
	TheaterRepository theaterRepository;
	@Autowired
	TheaterRoomRepository theaterRoomRepository;
	@Autowired
	SeatRepository seatRepository;

	@PersistenceContext
	EntityManager em;

	private Theater theater = new Theater(Region.SEOUL, "강남");
	private Set<RequestCreateSeat> requestCreateSeats = new HashSet<>();

	@BeforeEach
	void init() {
		requestCreateSeats.add(new RequestCreateSeat(0, 0));
		theaterRepository.save(theater);
	}

	@AfterEach
	void clear() {
		requestCreateSeats.clear();
	}

	@Test
	@DisplayName("새로운 상영관을 추가할 수 있다.")
	void testSave() {
		RequestCreateTheaterRoom requestCreateTheaterRoom = new RequestCreateTheaterRoom(theater.getId(), "A관",
			requestCreateSeats);
		int expectTotalCount = requestCreateSeats.size();
		Long savedId = theaterRoomService.save(requestCreateTheaterRoom);

		Optional<TheaterRoom> findTheaterRoom = theaterRoomRepository.findById(savedId);
		List<Seat> findSeats = seatRepository.findByTheaterRoomId(savedId);

		assertAll(
			() -> assertThat(findTheaterRoom).isPresent(),
			() -> assertThat(findSeats).hasSize(expectTotalCount)
		);
	}

	@Test
	@DisplayName("하나의 상영관에는 동일한 row, col을 가진 좌석이 존재할 수 없다.")
	void testSave_distinct() {
		requestCreateSeats.add(new RequestCreateSeat(0, 1));
		requestCreateSeats.add(new RequestCreateSeat(0, 2));
		requestCreateSeats.add(new RequestCreateSeat(0, 1));
		requestCreateSeats.add(new RequestCreateSeat(0, 2));
		requestCreateSeats.add(new RequestCreateSeat(0, 1));
		requestCreateSeats.add(new RequestCreateSeat(0, 2));

		RequestCreateTheaterRoom requestCreateTheaterRoom = new RequestCreateTheaterRoom(theater.getId(), "A관",
			requestCreateSeats);
		int expectTotalCount = 3;
		Long savedId = theaterRoomService.save(requestCreateTheaterRoom);
		List<Seat> findSeats = seatRepository.findByTheaterRoomId(savedId);

		assertThat(findSeats).hasSize(expectTotalCount);
	}

	@Test
	@DisplayName("존재하지 않는 Theater의 요청은 EntityNotFoundException 발생")
	void testSave_entityNotFoundException() {
		RequestCreateTheaterRoom requestCreateTheaterRoom = new RequestCreateTheaterRoom(-1L, "A관", requestCreateSeats);

		assertThrows(IllegalArgumentException.class, () -> theaterRoomService.save(requestCreateTheaterRoom));
	}

	@Test
	@DisplayName("전체 상영관 정보 조회")
	void testFindAll() {
		RequestCreateTheaterRoom requestCreateTheaterRoom = new RequestCreateTheaterRoom(
			theater.getId(),
			"A관",
			requestCreateSeats
		);

		RequestCreateTheaterRoom requestCreateTheaterRoom2 = new RequestCreateTheaterRoom(
			theater.getId(),
			"B관",
			requestCreateSeats
		);

		theaterRoomService.save(requestCreateTheaterRoom);
		theaterRoomService.save(requestCreateTheaterRoom2);
		em.flush();
		em.clear();

		List<ResponseFindTheaterRoom> theaterRoomList = theaterRoomService.findAll();
		assertAll(
			() -> assertThat(theaterRoomList).hasSize(2),
			() -> assertThat(theaterRoomList.get(0).totalCount()).isEqualTo(1),
			() -> assertThat(theaterRoomList.get(1).totalCount()).isEqualTo(1)
		);
	}
}

