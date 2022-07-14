package prgrms.marco.be02marbox.domain.theater.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.config.QueryDslConfig;
import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;

@DataJpaTest
@Import(QueryDslConfig.class)
class ScheduleRepositoryTest {

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private TheaterRepository theaterRepository;

	@Autowired
	private TheaterRoomRepository theaterRoomRepository;

	@Autowired
	private MovieRepository movieRepository;

	private Theater theater;
	private TheaterRoom theaterRoom;
	private Movie movie;

	@BeforeEach
	void setup() {
		theater = new Theater(Region.SEOUL, "강남");
		theaterRepository.save(theater);
		theaterRoom = new TheaterRoom(theater, "A관");
		theaterRoomRepository.save(theaterRoom);
		movie = new Movie("test", LimitAge.ADULT, Genre.ACTION, 100);
		movieRepository.save(movie);
	}

	@Test
	@DisplayName("원하는 두 날짜 사이의 스케줄 리스트 가져오기 테스트")
	void testFindSchedulesBetweenStartDateAndEndDate() {
		createAndSaveSchedule(theaterRoom, movie, LocalDateTime.now(), LocalDateTime.now());
		createAndSaveSchedule(theaterRoom, movie, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2));
		createAndSaveSchedule(theaterRoom, movie, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3));

		List<Schedule> schedules = scheduleRepository.findSchedulesBetweenStartDateAndEndDate(LocalDate.now(),
			LocalDate.now().plusDays(2));

		assertThat(schedules.size()).isEqualTo(2);
	}

	@Test
	void testFindSchedulesByDate() {
		Movie movie2 = createAndSaveTempMovieInstance("영화2");
		Movie movie3 = createAndSaveTempMovieInstance("영화3");
		Movie movie4 = createAndSaveTempMovieInstance("영화4");

		createAndSaveSchedule(theaterRoom, movie, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1));
		createAndSaveSchedule(theaterRoom, movie, LocalDateTime.now(), LocalDateTime.now());
		createAndSaveSchedule(theaterRoom, movie, LocalDateTime.now(), LocalDateTime.now());
		createAndSaveSchedule(theaterRoom, movie2, LocalDateTime.now(), LocalDateTime.now());
		createAndSaveSchedule(theaterRoom, movie3, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2));
		createAndSaveSchedule(theaterRoom, movie4, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3));

		List<Schedule> todaySchedules = scheduleRepository.findScheduleByDate(LocalDate.now());

		assertThat(todaySchedules).hasSize(3);
	}

	@Test
	@DisplayName("영화 ID와 날짜로 스케줄 조회 테스트")
	void testFindSchedulesByMovieIdAndDate() {
		TheaterRoom theaterRoom2 = new TheaterRoom(theater, "상영관2");
		theaterRoomRepository.save(theaterRoom2);
		Movie movie2 = createAndSaveTempMovieInstance("영화2");

		createAndSaveSchedule(theaterRoom, movie,
			LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 30)),
			LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 30)));
		createAndSaveSchedule(theaterRoom, movie2,
			LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 30)),
			LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 30)));
		createAndSaveSchedule(theaterRoom, movie,
			LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)),
			LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 30)));
		createAndSaveSchedule(theaterRoom2, movie,
			LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0)),
			LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0)));
		createAndSaveSchedule(theaterRoom2, movie2,
			LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 30)),
			LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 30)));
		createAndSaveSchedule(theaterRoom2, movie,
			LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(12, 30)),
			LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 30)));

		Long movieId = movie.getId();
		LocalDate searchDate = LocalDate.now();
		List<Schedule> schedules = scheduleRepository.findSchedulesByMovieIdAndDate(movieId, searchDate);

		assertThat(schedules).hasSize(2);
	}

	private Movie createAndSaveTempMovieInstance(String name) {
		Movie movie = new Movie(name, LimitAge.ADULT, Genre.ACTION, 100);
		movieRepository.save(movie);
		return movie;
	}

	private void createAndSaveSchedule(TheaterRoom theaterRoom, Movie movie, LocalDateTime startTime,
		LocalDateTime endTime) {
		Schedule schedule = Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(startTime)
			.endTime(endTime)
			.build();

		scheduleRepository.save(schedule);
	}
}
