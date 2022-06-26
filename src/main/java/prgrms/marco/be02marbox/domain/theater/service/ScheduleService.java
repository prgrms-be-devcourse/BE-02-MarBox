package prgrms.marco.be02marbox.domain.theater.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindCurrentMovie;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.ScheduleConverter;

@Service
public class ScheduleService {

	private static final int CURRENT_SCHEDULE_PERIOD = 4;
	private static final LocalDate CURRENT_SCHEDULE_START_DATE = LocalDate.now();
	private static final LocalDate CURRENT_SCHEDULE_END_DATE = LocalDate.now().plusDays(CURRENT_SCHEDULE_PERIOD);

	private final ScheduleRepository scheduleRepository;
	private final ScheduleConverter converter;
	private final TheaterRoomRepository theaterRoomRepository;
	private final MovieRepository movieRepository;

	public ScheduleService(ScheduleRepository scheduleRepository,
		ScheduleConverter converter,
		TheaterRoomRepository theaterRoomRepository,
		MovieRepository movieRepository) {
		this.scheduleRepository = scheduleRepository;
		this.converter = converter;
		this.theaterRoomRepository = theaterRoomRepository;
		this.movieRepository = movieRepository;
	}

	@Transactional
	public Long createSchedule(RequestCreateSchedule requestCreateSchedule) {
		TheaterRoom theaterRoom = theaterRoomRepository.findById(requestCreateSchedule.theaterRoomId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상영관 ID"));
		Movie movie = movieRepository.findById(requestCreateSchedule.movieId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화 ID"));

		Schedule schedule = converter.convertFromRequestCreateScheduleToSchedule(requestCreateSchedule, theaterRoom,
			movie);

		Schedule savedSchedule = scheduleRepository.save(schedule);

		return savedSchedule.getId();
	}

	@Transactional(readOnly = true)
	public List<ResponseFindCurrentMovie> getCurrentMovieList() {
		List<Schedule> scheduleList = scheduleRepository.getSchedulesBetweenStartDateAndEndDate(
			CURRENT_SCHEDULE_START_DATE,
			CURRENT_SCHEDULE_END_DATE);

		return scheduleList.stream()
			.map(schedule ->
				new ResponseFindCurrentMovie(schedule.getMovie().getName(), schedule.getMovie().getLimitAge(),
					schedule.getMovie().getGenre(), schedule.getMovie().getRunningTime(),
					schedule.getMovie().getPosterImgLocation())
			)
			.distinct()
			.collect(Collectors.toList());
	}

}
