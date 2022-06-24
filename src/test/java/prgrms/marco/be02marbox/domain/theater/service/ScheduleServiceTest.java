package prgrms.marco.be02marbox.domain.theater.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.ScheduleConverter;

@DataJpaTest
@Import({ScheduleService.class, ScheduleConverter.class})
class ScheduleServiceTest {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private TheaterRoomRepository theaterRoomRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private TheaterRepository theaterRepository;

	@Autowired
	private ScheduleService scheduleService;

	private TheaterRoom theaterRoom;

	private Movie movie;

	@BeforeEach
	void setup() {
		Theater theater = new Theater(Region.SEOUL, "강남");
		theaterRepository.save(theater);
		theaterRoom = new TheaterRoom(theater, "A관");
		theaterRoomRepository.save(theaterRoom);
		movie = new Movie("test", LimitAge.ADULT, Genre.ACTION, 100, "test");
		movieRepository.save(movie);
	}

	@Test
	@DisplayName("스케줄 생성 성공 테스트")
	void testCreateSchedule() {
		RequestCreateSchedule requestCreateSchedule = new RequestCreateSchedule(theaterRoom.getId(), movie.getId(),
			LocalDateTime.now(),
			LocalDateTime.now());

		Long id = scheduleService.createSchedule(requestCreateSchedule);

		Optional<Schedule> findSchedule = scheduleRepository.findById(id);

		assertAll(
			() -> assertThat(findSchedule).isPresent(),
			() -> assertThat(id).isEqualTo(findSchedule.get().getId()),
			() -> assertThat(movie.getId()).isEqualTo(findSchedule.get().getMovie().getId())
		);
	}

	@Test
	@DisplayName("TheaterRoom이 없을 경우 스케줄 생성 실패")
	void testCreateScheduleFail_TheaterRoom_No_Exists() {
		RequestCreateSchedule requestCreateSchedule = new RequestCreateSchedule(100L, movie.getId(),
			LocalDateTime.now(),
			LocalDateTime.now());

		assertThrows(IllegalArgumentException.class, () -> {
			scheduleService.createSchedule(requestCreateSchedule);
		});
	}

	@Test
	@DisplayName("Movie가 없을 경우 스케줄 생성 실패")
	void testCreateScheduleFail_Movie_No_Exists() {
		RequestCreateSchedule requestCreateSchedule = new RequestCreateSchedule(theaterRoom.getId(), 100L,
			LocalDateTime.now(),
			LocalDateTime.now());

		assertThrows(IllegalArgumentException.class, () -> {
			scheduleService.createSchedule(requestCreateSchedule);
		});
	}

}
