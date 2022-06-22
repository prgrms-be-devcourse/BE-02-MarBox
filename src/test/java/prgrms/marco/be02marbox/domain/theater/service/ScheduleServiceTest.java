package prgrms.marco.be02marbox.domain.theater.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.ScheduleRecord;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private TheaterRoomRepository theaterRoomRepository;

	@Mock
	private ScheduleRepository scheduleRepository;

	@InjectMocks
	private ScheduleService scheduleService;

	@Test
	@DisplayName("스케줄 생성 성공 테스트")
	void testCreateSchedule() {
		ScheduleRecord scheduleRecord = new ScheduleRecord(1L, 1L, LocalDateTime.now(), LocalDateTime.now());

		TheaterRoom theaterRoom = new TheaterRoom();
		given(theaterRoomRepository.findById(anyLong())).willReturn(Optional.of(theaterRoom));

		Movie movie = new Movie();
		given(movieRepository.findById(anyLong())).willReturn(Optional.of(movie));

		Schedule schedule = Schedule.builder()
			.id(1L)
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(scheduleRecord.startTime())
			.endTime(scheduleRecord.endTime())
			.build();
		given(scheduleRepository.save(any(Schedule.class))).willReturn(schedule);

		Long id = scheduleService.createSchedule(scheduleRecord);

		assertThat(id).isEqualTo(1L);
	}

}
