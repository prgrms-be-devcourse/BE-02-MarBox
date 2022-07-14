package prgrms.marco.be02marbox.domain.reservation.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import prgrms.marco.be02marbox.config.QueryDslConfig;
import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.SeatRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

/**
 * Entity 저장을 위한 클래스, 데이터 저장 후 영속성 컨텍스트를 비운다. clear 호출..
 *
 * save{Entity} 함수는 1개의 고정된 데이터의 Entity 를 저장 후 반환한다. (unique 컬럼이 존재하면 2번 호출 시 Exception)
 */
@DataJpaTest
@Import(QueryDslConfig.class)
public class RepositoryTestUtil {

	private static final int MAX_ROW = 10;
	private static final int MAX_COUNT = (MAX_ROW * MAX_ROW);

	@PersistenceContext
	protected EntityManager em;

	@Autowired
	public ReservedSeatRepository reservedSeatRepository;

	@Autowired
	public TicketRepository ticketRepository;

	@Autowired
	public SeatRepository seatRepository;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public TheaterRepository theaterRepository;

	@Autowired
	public TheaterRoomRepository theaterRoomRepository;

	@Autowired
	public MovieRepository movieRepository;

	@Autowired
	public ScheduleRepository scheduleRepository;

	/**
	 *  Seat
	 */

	public Seat saveSeat() {
		TheaterRoom theaterRoom = saveTheaterRoom("A관");
		Seat seat = new Seat(theaterRoom, 0, 0);
		return seatRepository.save(seat);
	}

	/**
	 * 상영관, 좌석을 지정하여 데이터를 저장한다.
	 * @param theaterRoom 상영관
	 * @param row 등록될 행
	 * @param col 등록될
	 * @return Seat 생성된 좌석
	 */
	public Seat saveSeat(TheaterRoom theaterRoom, int row, int col) {
		Seat seat = new Seat(theaterRoom, row, col);
		Seat savedSeat = seatRepository.save(seat);
		clear();
		return savedSeat;
	}

	/**
	 * 1개 상영관에 포함하는 여러 좌석을 저장한다.
	 * @param seatCount 좌석 수
	 * @return TheaterRoom 생성된 상영관 정보
	 */
	public TheaterRoom saveSeatMulti(int seatCount) {
		seatCount = validateSeatCount(seatCount);

		TheaterRoom theaterRoom = saveTheaterRoom("A관");
		IntStream.range(0, seatCount).forEach((seq) ->
			saveSeat(theaterRoom, seqToRow(seq), seqToCol(seq))
		);
		return theaterRoom;
	}

	/**
	 *  TheaterRoom
	 */

	public TheaterRoom saveTheaterRoom(String name) {
		Theater theater = saveTheater("강남");
		TheaterRoom theaterRoom = new TheaterRoom(theater, name);
		TheaterRoom savedTheaterRoom = theaterRoomRepository.save(theaterRoom);
		clear();
		return savedTheaterRoom;
	}

	/**
	 *  Theater
	 */

	public Theater saveTheater(String name) {
		Theater theater = new Theater(Region.SEOUL, name);
		Theater savedTheater = theaterRepository.save(theater);
		clear();
		return savedTheater;
	}

	/**
	 *  User
	 */

	public User saveUser() {
		User user = new User(
			UUID.randomUUID() + "@email.com",
			"1234",
			"test",
			Role.ROLE_CUSTOMER);
		User savedUser = userRepository.save(user);
		clear();
		return savedUser;
	}

	/**
	 *  Ticket
	 */

	public Ticket saveTicket() {
		User user = saveUser();
		Schedule schedule = saveSchedule();
		Ticket ticket = new Ticket(user, schedule, LocalDateTime.now());
		Ticket savedTicket = ticketRepository.save(ticket);
		clear();
		return savedTicket;
	}

	public Ticket saveTicket(Schedule schedule) {
		User user = saveUser();
		Ticket ticket = new Ticket(user, schedule, LocalDateTime.now());

		Ticket savedTicket = ticketRepository.save(ticket);
		clear();
		return savedTicket;
	}

	/**
	 *  Schedule
	 */

	public Schedule saveSchedule() {
		Movie movie = saveMovie("범죄도시2");
		TheaterRoom theaterRoom = saveTheaterRoom("A관");
		Schedule schedule = Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now())
			.build();
		Schedule savedSchedule = scheduleRepository.save(schedule);
		clear();
		return savedSchedule;
	}

	public Schedule saveSchedule(TheaterRoom theaterRoom) {
		Movie movie = saveMovie("범죄도시2");
		Schedule schedule = Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now())
			.build();
		Schedule savedSchedule = scheduleRepository.save(schedule);
		clear();
		return savedSchedule;
	}

	/**
	 *  Movie
	 */

	public Movie saveMovie(String name) {
		Movie movie = new Movie(name, LimitAge.ADULT, Genre.ACTION, 100);
		Movie savedMovie = movieRepository.save(movie);
		clear();
		return savedMovie;
	}

	/**
	 *  ReservedSeat
	 */

	public ReservedSeat saveReservedSeat() {
		Ticket ticket = saveTicket();
		Seat seat = saveSeat();

		ReservedSeat reservedSeat = new ReservedSeat(ticket, seat);
		ReservedSeat savedReservedSeat = reservedSeatRepository.save(reservedSeat);
		clear();
		return savedReservedSeat;
	}

	/**
	 * 1개의 티켓에 여러개 좌석을 예매하는 데이터를 저장한다.
	 * @param seatCount 좌석 수
	 * @return Schedule 티켓의 스케줄 정보
	 */
	public Schedule saveReservedSeatMultiSeat(int seatCount) {
		seatCount = validateSeatCount(seatCount);

		Ticket ticket = saveTicket();
		IntStream.range(0, seatCount).forEach((seq) -> {
			Seat seat = saveSeat(ticket.getSchedule().getTheaterRoom(), seqToRow(seq), seqToCol(seq));
			ReservedSeat reservedSeat = new ReservedSeat(ticket, seat);
			reservedSeatRepository.save(reservedSeat);
		});
		clear();
		return ticket.getSchedule();
	}

	/**
	 * 상영관에 totalSeatCount 만큼 좌석을 만들고
	 * 그 중 reserveCount 만큼 예약한다.
	 *
	 * @param totalSeatCount 저장 할 좌석의 개수
	 * @param reserveCount 예약 할 좌석의 개수
	 * @return 스케줄
	 */
	public Schedule saveSeatAndReserveSeat(int totalSeatCount, int reserveCount) {
		// seatCount 좌석 저장
		TheaterRoom theaterRoom = saveSeatMulti(totalSeatCount);
		List<Seat> savedSeats = seatRepository.findByTheaterRoomId(theaterRoom.getId());

		List<Seat> rserveSeatList = new ArrayList<>();
		// 그 중 reserveCount 만큼 좌석을 예약한다.
		for (int i = 0; i < reserveCount; i++) {
			rserveSeatList.add(savedSeats.get(i));
		}
		Schedule schedule = saveSchedule(theaterRoom);
		Ticket ticket = saveTicket(schedule);
		rserveSeatList.forEach((seat) -> {
			ReservedSeat reservedSeat = new ReservedSeat(ticket, seat);
			reservedSeatRepository.save(reservedSeat);
		});
		clear();
		return schedule;
	}

	/**
	 * private 함수
	 */
	private int validateSeatCount(int seatCount) {
		return Math.min(seatCount, MAX_COUNT);
	}

	private int seqToRow(int seq) {
		return (seq / MAX_ROW);
	}

	private int seqToCol(int seq) {
		return (seq % MAX_ROW);
	}

	private void clear() {
		em.flush();
		em.clear();
	}
}
