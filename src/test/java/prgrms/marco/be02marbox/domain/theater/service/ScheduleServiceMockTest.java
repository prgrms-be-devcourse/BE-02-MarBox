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
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.ScheduleConverter;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceMockTest {

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private TheaterRoomRepository theaterRoomRepository;

	@Mock
	private ScheduleRepository scheduleRepository;

	@Mock
	private ScheduleConverter scheduleConverter;

	@InjectMocks
	private ScheduleService scheduleService;

	@Test
	@DisplayName("스케줄 생성 성공 테스트")
	void testCreateSchedule() {
		RequestCreateSchedule requestCreateSchedule = new RequestCreateSchedule(1L, 1L, LocalDateTime.now(),
			LocalDateTime.now());

		Theater theater = new Theater(Region.SEOUL, "강남");
		TheaterRoom theaterRoom = new TheaterRoom(theater, "A관");
		given(theaterRoomRepository.findById(anyLong())).willReturn(Optional.of(theaterRoom));

		Movie movie = new Movie(null, null, null, null, null);
		given(movieRepository.findById(anyLong())).willReturn(Optional.of(movie));

		Schedule schedule = Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(requestCreateSchedule.startTime())
			.endTime(requestCreateSchedule.endTime())
			.build();
		given(
			scheduleConverter.convertFromRequestCreateScheduleToschdeule(any(RequestCreateSchedule.class),
				any(TheaterRoom.class),
				any(Movie.class))).willReturn(
			schedule);
		given(scheduleRepository.save(any(Schedule.class))).willReturn(schedule);

		Long id = scheduleService.createSchedule(requestCreateSchedule);

		assertThat(id).isEqualTo(schedule.getId());
	}

}
