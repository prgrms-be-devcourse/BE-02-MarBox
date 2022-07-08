package prgrms.marco.be02marbox.domain.theater.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

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

	/**
	 * 스케줄 생성
	 * @param requestCreateSchedule
	 * @return 생성된 스케줄 id
	 */
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

	/**
	 * 현재 상영하는 전체 영화 조회
	 * @return 영화 리스트
	 */
	@Transactional(readOnly = true)
	public List<ResponseFindMovie> findShowingMovieList() {
		List<Schedule> showingMoviesSchedules = findShowingMoviesSchedules();

		return showingMoviesSchedules.stream()
			.map(schedule -> movieConverter.convertFromMovieToResponseFindMovie(schedule.getMovie()))
			.distinct()
			.collect(Collectors.toList());
	}

	/**
	 * 영화관에서 상영하는 영화와 날짜 조회
	 * @param theaterId
	 * @return 영화 리스트, 날짜 리스트
	 */
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

	/**
	 * 영화관에서 특정 날짜에 상영하는 영화 조회
	 * @param theaterId
	 * @param date
	 * @return 영화 리스트
	 */
	@Transactional(readOnly = true)
	public ResponseFindSchedule findMovieListByTheaterIdAndDate(Long theaterId, LocalDate date) {
		if (!isValidateDate(date)) {
			throw new DateTimeException(Message.INVALID_DATE_EXP_MSG.getMessage());
		}
		theaterRepository.findById(theaterId)
			.orElseThrow(() -> new EntityNotFoundException(Message.INVALID_THEATER_EXP_MSG.getMessage()));

		Set<TheaterRoom> theaterRoomsOfTheater = theaterRoomRepository.findAllByTheaterId(theaterId);
		List<Schedule> schedulesOfDate = scheduleRepository.findScheduleByDate(date);

		List<ResponseFindMovie> movieList = schedulesOfDate.stream()
			.filter(schedule -> theaterRoomsOfTheater.contains(schedule.getTheaterRoom()))
			.map(schedule -> movieConverter.convertFromMovieToResponseFindMovie(schedule.getMovie()))
			.distinct().toList();

		return new ResponseFindSchedule(movieList, Collections.emptyList(), Collections.emptyList(),
			Collections.emptyList());
	}

	/**
	 * 특정 스케줄 조회
	 * @param scheduleId 스케줄 id
	 * @return 스케줄 정보
	 */
	@Transactional(readOnly = true)
	public Schedule findById(Long scheduleId) {
		return scheduleRepository.findById(scheduleId)
			.orElseThrow(() -> new IllegalArgumentException(INVALID_SCHEDULE_EXP_MSG.getMessage()));
	}

	/**
	 * 시간 스케줄 조회
	 * @param movieId
	 * @param theaterId
	 * @param date
	 * @return 시간 리스트
	 */
	@Transactional(readOnly = true)
	public ResponseFindSchedule findTimeScheduleList(Long movieId, Long theaterId, LocalDate date) {
		if (!isValidateDate(date)) {
			throw new DateTimeException(Message.INVALID_DATE_EXP_MSG.getMessage());
		}
		movieRepository.findById(movieId)
			.orElseThrow(() -> new IllegalArgumentException(Message.INVALID_MOVIE_EXP_MSG.getMessage()));
		theaterRepository.findById(theaterId)
			.orElseThrow(() -> new EntityNotFoundException(Message.INVALID_THEATER_EXP_MSG.getMessage()));

		List<Schedule> schedulesOfAllTheaters = scheduleRepository.findSchedulesByMovieIdAndDate(movieId, date);
		Set<TheaterRoom> theaterRoomsOfTheater = theaterRoomRepository.findAllByTheaterId(theaterId);

		List<Schedule> schedulesInTheater = schedulesOfAllTheaters.stream()
			.filter(schedule -> theaterRoomsOfTheater.contains(schedule.getTheaterRoom()))
			.toList();

		List<ResponseFindTime> timeList = scheduleConverter
			.convertFromScheduleListToResponseFindTimeList(schedulesInTheater);

		return new ResponseFindSchedule(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
			timeList);
	}

	/**
	 * 영화관에서 영화를 상영하는 날짜인지 확인
	 * @param date
	 * @return 상영 정보 유무
	 */
	private boolean isValidateDate(LocalDate date) {
		return !LocalDate.now().plusDays(CURRENT_SCHEDULE_PERIOD).isBefore(date) && !LocalDate.now().isAfter(date);
	}

	/**
	 * 현재 상영하는 영화들의 스케줄 조회
	 * @return 스케줄 리스트
	 */
	private List<Schedule> findShowingMoviesSchedules() {
		return scheduleRepository.findSchedulesBetweenStartDateAndEndDate(
			LocalDate.now(),
			LocalDate.now().plusDays(CURRENT_SCHEDULE_PERIOD));
	}

}
