package prgrms.marco.be02marbox.domain.theater.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindCurrentMovie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.ScheduleConverter;

@Service
public class ScheduleService {

	private static final int CURRENT_SCHEDULE_PERIOD = 19;

	private final ScheduleRepository scheduleRepository;
	private final ScheduleConverter scheduleConverter;
	private final TheaterRoomRepository theaterRoomRepository;
	private final MovieRepository movieRepository;
	private final MovieConverter movieConverter;

	public ScheduleService(ScheduleRepository scheduleRepository,
		ScheduleConverter converter,
		TheaterRoomRepository theaterRoomRepository,
		MovieRepository movieRepository,
		MovieConverter movieConverter) {
		this.scheduleRepository = scheduleRepository;
		this.scheduleConverter = converter;
		this.theaterRoomRepository = theaterRoomRepository;
		this.movieRepository = movieRepository;
		this.movieConverter = movieConverter;
	}

	@Transactional
	public Long createSchedule(RequestCreateSchedule requestCreateSchedule) {
		TheaterRoom theaterRoom = theaterRoomRepository.findById(requestCreateSchedule.theaterRoomId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상영관 ID"));
		Movie movie = movieRepository.findById(requestCreateSchedule.movieId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화 ID"));

		Schedule schedule = scheduleConverter.convertFromRequestCreateScheduleToSchedule(requestCreateSchedule,
			theaterRoom,
			movie);

		Schedule savedSchedule = scheduleRepository.save(schedule);

		return savedSchedule.getId();
	}

	@Transactional(readOnly = true)
	public List<ResponseFindCurrentMovie> getCurrentMovieList() {
		List<Schedule> scheduleList = scheduleRepository.getSchedulesBetweenStartDateAndEndDate(
			LocalDate.now(),
			LocalDate.now().plusDays(CURRENT_SCHEDULE_PERIOD));

		return scheduleList.stream()
			.map(schedule -> movieConverter.convertFromMovieToResponseFindCurrentMovie(schedule.getMovie()))
			.distinct()
			.collect(Collectors.toList());
	}

}
