package prgrms.marco.be02marbox.domain.theater.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.exception.custom.Message;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindMovie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.movie.service.utils.MovieConverter;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTime;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.ScheduleConverter;

@Service
public class ScheduleService {

	private static final int CURRENT_SCHEDULE_PERIOD = 19;

	private final ScheduleRepository scheduleRepository;
	private final ScheduleConverter scheduleConverter;
	private final TheaterRepository theaterRepository;
	private final TheaterRoomRepository theaterRoomRepository;
	private final MovieRepository movieRepository;
	private final MovieConverter movieConverter;

	public ScheduleService(ScheduleRepository scheduleRepository,
		ScheduleConverter converter,
		TheaterRepository theaterRepository,
		TheaterRoomRepository theaterRoomRepository,
		MovieRepository movieRepository,
		MovieConverter movieConverter) {
		this.scheduleRepository = scheduleRepository;
		this.scheduleConverter = converter;
		this.theaterRepository = theaterRepository;
		this.theaterRoomRepository = theaterRoomRepository;
		this.movieRepository = movieRepository;
		this.movieConverter = movieConverter;
	}

	@Transactional
	public Long createSchedule(RequestCreateSchedule requestCreateSchedule) {
		TheaterRoom theaterRoom = theaterRoomRepository.findById(requestCreateSchedule.theaterRoomId())
			.orElseThrow(() -> new IllegalArgumentException(Message.INVALID_THEATER_ROOM_EXP_MSG.getMessage()));
		Movie movie = movieRepository.findById(requestCreateSchedule.movieId())
			.orElseThrow(() -> new IllegalArgumentException(Message.INVALID_MOVIE_EXP_MSG.getMessage()));

		Schedule schedule = scheduleConverter.convertFromRequestCreateScheduleToSchedule(requestCreateSchedule,
			theaterRoom,
			movie);

		Schedule savedSchedule = scheduleRepository.save(schedule);

		return savedSchedule.getId();
	}

	@Transactional(readOnly = true)
	public List<ResponseFindMovie> findShowingMovieList() {
		List<Schedule> showingMoviesSchedules = findShowingMoviesSchedules();

		return showingMoviesSchedules.stream()
			.map(schedule -> movieConverter.convertFromMovieToResponseFindMovie(schedule.getMovie()))
			.distinct()
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ResponseFindSchedule findMovieListAndDateListByTheaterId(Long theaterId) {
		theaterRepository.findById(theaterId).orElseThrow(
			() -> new EntityNotFoundException(Message.INVALID_THEATER_EXP_MSG.getMessage()));
		Set<TheaterRoom> theaterRooms = theaterRoomRepository.findAllByTheaterId(theaterId);
		List<Schedule> showingMoviesSchedules = findShowingMoviesSchedules();

		List<Schedule> theaterSchedules = showingMoviesSchedules.stream()
			.filter(schedule -> theaterRooms.contains(schedule.getTheaterRoom()))
			.toList();

		List<LocalDate> dateList = scheduleConverter.convertFromScheduleListToDateList(theaterSchedules);
		List<ResponseFindMovie> movieList = theaterSchedules.stream()
			.map(schedule -> movieConverter.convertFromMovieToResponseFindMovie(schedule.getMovie()))
			.distinct()
			.toList();

		return new ResponseFindSchedule(movieList, Collections.emptyList(), dateList, Collections.emptyList());
	}

	@Transactional(readOnly = true)
	public ResponseFindSchedule findMovieListByTheaterIdAndDate(Long theaterId, LocalDate date) {
		theaterRepository.findById(theaterId)
			.orElseThrow(() -> new EntityNotFoundException(Message.INVALID_THEATER_EXP_MSG.getMessage()));
		if (LocalDate.now().plusDays(CURRENT_SCHEDULE_PERIOD).isBefore(date) || LocalDate.now().isAfter(date)) {
			throw new DateTimeException(Message.INVALID_DATE_EXP_MSG.getMessage());
		}

		Set<TheaterRoom> theaterRooms = theaterRoomRepository.findAllByTheaterId(theaterId);
		List<Schedule> schedulesOfDate = scheduleRepository.findScheduleByDate(date);

		List<ResponseFindMovie> movieList = schedulesOfDate.stream()
			.filter(schedule -> theaterRooms.contains(schedule.getTheaterRoom()))
			.map(schedule -> movieConverter.convertFromMovieToResponseFindMovie(schedule.getMovie()))
			.distinct().toList();

		return new ResponseFindSchedule(movieList, Collections.emptyList(), Collections.emptyList(),
			Collections.emptyList());
	}

	@Transactional(readOnly = true)
	public ResponseFindSchedule findTimeScheduleList(Long movieId, Long theaterId, LocalDate date) {
		movieRepository.findById(movieId)
			.orElseThrow(() -> new IllegalArgumentException(Message.INVALID_MOVIE_EXP_MSG.getMessage()));
		theaterRepository.findById(theaterId)
			.orElseThrow(() -> new EntityNotFoundException(Message.INVALID_THEATER_EXP_MSG.getMessage()));
		if (LocalDate.now().plusDays(CURRENT_SCHEDULE_PERIOD).isBefore(date) || LocalDate.now().isAfter(date)) {
			throw new DateTimeException(Message.INVALID_DATE_EXP_MSG.getMessage());
		}

		List<Schedule> schedulesOfAllTheaters = scheduleRepository.findSchedulesByMovieIdAndDate(movieId, date);
		Set<TheaterRoom> theaterRoomsOfRequestTheater = theaterRoomRepository.findAllByTheaterId(theaterId);

		List<Schedule> schedulesInRequestTheater = schedulesOfAllTheaters.stream()
			.filter(schedule -> theaterRoomsOfRequestTheater.contains(schedule.getTheaterRoom()))
			.toList();

		List<ResponseFindTime> startTimeList = scheduleConverter
			.convertFromScheduleInTheaterToResponseFindTimeList(schedulesInRequestTheater,
				theaterRoomsOfRequestTheater);

		return new ResponseFindSchedule(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
			startTimeList);
	}

	private List<Schedule> findShowingMoviesSchedules() {
		return scheduleRepository.findSchedulesBetweenStartDateAndEndDate(
			LocalDate.now(),
			LocalDate.now().plusDays(CURRENT_SCHEDULE_PERIOD));
	}

}
