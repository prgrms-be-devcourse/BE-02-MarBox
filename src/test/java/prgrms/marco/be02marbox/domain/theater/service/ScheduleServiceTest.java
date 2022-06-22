package prgrms.marco.be02marbox.domain.theater.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
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
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.ScheduleRecord;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;

@DataJpaTest
@Import(ScheduleService.class)
class ScheduleServiceTest {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private TheaterRoomRepository theaterRoomRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private ScheduleService scheduleService;

	private TheaterRoom theaterRoom;

	private Movie movie;

	@BeforeEach
	void setup() {
		theaterRoom = new TheaterRoom();
		theaterRoomRepository.save(theaterRoom);
		movie = new Movie("test", LimitAge.ADULT, Genre.ACTION, 100, "test");
		movieRepository.save(movie);
	}

	@AfterEach
	void clean() {
		movieRepository.deleteAll();
		theaterRoomRepository.deleteAll();
	}

	@Test
	@DisplayName("스케줄 생성 성공 테스트")
	void testCreateSchedule() {
		ScheduleRecord scheduleRecord = new ScheduleRecord(theaterRoom.getId(), movie.getId(), LocalDateTime.now(),
			LocalDateTime.now());

		Long id = scheduleService.createSchedule(scheduleRecord);

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
		ScheduleRecord scheduleRecord = new ScheduleRecord(100L, movie.getId(), LocalDateTime.now(),
			LocalDateTime.now());

		assertThrows(IllegalArgumentException.class, () -> {
			scheduleService.createSchedule(scheduleRecord);
		});
	}

	@Test
	@DisplayName("Movie가 없을 경우 스케줄 생성 실패")
	void testCreateScheduleFail_Movie_No_Exists() {
		ScheduleRecord scheduleRecord = new ScheduleRecord(theaterRoom.getId(), 100L, LocalDateTime.now(),
			LocalDateTime.now());

		assertThrows(IllegalArgumentException.class, () -> {
			scheduleService.createSchedule(scheduleRecord);
		});
	}

}
