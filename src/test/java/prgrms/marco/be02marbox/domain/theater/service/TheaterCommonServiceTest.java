package prgrms.marco.be02marbox.domain.theater.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.reservation.repository.RepositoryTestUtil;
import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSeat;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.service.utils.SeatConverter;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterConverter;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterRoomConverter;

@DataJpaTest
@Import({TheaterCommonService.class, TheaterService.class, TheaterRoomService.class,
	SeatConverter.class, TheaterConverter.class, TheaterRoomConverter.class})
class TheaterCommonServiceTest extends RepositoryTestUtil {

	@Autowired
	TheaterCommonService theaterCommonService;

	private Set<RequestCreateSeat> requestCreateSeats = Set.of(
		new RequestCreateSeat(0, 0),
		new RequestCreateSeat(0, 1)
	);

	private Theater theater;

	@BeforeEach
	void init() {
		theater = saveTheater("강남");
	}

	@AfterEach
	void clear() {
		theaterRepository.deleteById(theater.getId());
	}

	@Test
	@DisplayName("새로운 상영관을 추가할 수 있다.")
	void testSaveTheaterRoomWithSeatList() {
		Theater theater = saveTheater("강남");
		RequestCreateTheaterRoom requestCreateTheaterRoom = new RequestCreateTheaterRoom(theater.getId(), "A관",
			requestCreateSeats);
		int expectTotalCount = requestCreateSeats.size();
		Long savedId = theaterCommonService.saveTheaterRoomWithSeatList(requestCreateTheaterRoom);

		Optional<TheaterRoom> findTheaterRoom = theaterRoomRepository.findById(savedId);
		List<Seat> findSeats = seatRepository.findByTheaterRoomId(savedId);
		assertAll(
			() -> assertThat(findTheaterRoom).isPresent(),
			() -> assertThat(findSeats).hasSize(expectTotalCount)
		);
	}

	@Test
	@DisplayName("존재하지 않는 Theater의 요청은 EntityNotFoundException 발생")
	void testSave_entityNotFoundException() {
		RequestCreateTheaterRoom requestCreateTheaterRoom =
			new RequestCreateTheaterRoom(-1L, "A관", requestCreateSeats);

		assertThrows(EntityNotFoundException.class,
			() -> theaterCommonService.saveTheaterRoomWithSeatList(requestCreateTheaterRoom));
	}
}
