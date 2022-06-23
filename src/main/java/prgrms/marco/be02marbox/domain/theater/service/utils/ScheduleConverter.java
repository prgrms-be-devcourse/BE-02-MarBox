package prgrms.marco.be02marbox.domain.theater.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;

@Component
public class ScheduleConverter {

	private final TheaterRoomRepository theaterRoomRepository;
	private final MovieRepository movieRepository;

	public ScheduleConverter(
		TheaterRoomRepository theaterRoomRepository,
		MovieRepository movieRepository) {
		this.theaterRoomRepository = theaterRoomRepository;
		this.movieRepository = movieRepository;
	}

	public Schedule convertFromRequestCreateScheduleToSchdeule(RequestCreateSchedule request) {
		TheaterRoom theaterRoom = theaterRoomRepository.findById(request.theaterRoomId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상영관 ID"));
		Movie movie = movieRepository.findById(request.movieId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화 ID"));

		Schedule schedule = Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(request.startTime())
			.endTime(request.endTime())
			.build();

		return schedule;
	}
}
