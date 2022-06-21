package prgrms.marco.be02marbox.domain.theater.service;

import org.springframework.stereotype.Service;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.ScheduleRequestDto;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;

@Service
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final TheaterRoomRepository theaterRoomRepository;
	private final MovieRepository movieRepository;

	public ScheduleService(ScheduleRepository scheduleRepository,
		TheaterRoomRepository theaterRoomRepository,
		MovieRepository movieRepository) {
		this.scheduleRepository = scheduleRepository;
		this.theaterRoomRepository = theaterRoomRepository;
		this.movieRepository = movieRepository;
	}

	public Long createSchedule(ScheduleRequestDto scheduleRequestDto) {
		TheaterRoom theaterRoom = theaterRoomRepository.findById(scheduleRequestDto.getTheaterRoomId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상영관 ID"));
		Movie movie = movieRepository.findById(scheduleRequestDto.getMovieId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화 ID"));

		Schedule schedule = Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(scheduleRequestDto.getStartTime())
			.endTime(scheduleRequestDto.getEndTime())
			.build();

		Schedule savedSchedule = scheduleRepository.save(schedule);

		return savedSchedule.getId();
	}

}
