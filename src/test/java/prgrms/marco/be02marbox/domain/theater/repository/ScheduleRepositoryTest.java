package prgrms.marco.be02marbox.domain.theater.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;

@DataJpaTest
class ScheduleRepositoryTest {

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private TheaterRepository theaterRepository;

	@Autowired
	private TheaterRoomRepository theaterRoomRepository;

	@Autowired
	private MovieRepository movieRepository;

	private TheaterRoom theaterRoom;
	private Movie movie;

	@BeforeEach
	void setup() {
		Theater theater = new Theater(Region.SEOUL, "강남");
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
