package prgrms.marco.be02marbox.domain.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
 * Entity 저장을 위한 클래스
 *
 * save{Entity} 함수는 1개의 고정된 데이터의 Entity 를 저장 후 반환한다. (unique 컬럼이 존재하면 2번 호출 시 Exception)
 */
@DataJpaTest
public class RepositoryTestUtil {

	private static final int MAX_ROW = 10;
	private static final int MAX_COUNT = (MAX_ROW * MAX_ROW);

	@PersistenceContext
	public EntityManager em;

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
		return seatRepository.save(seat);
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
		return theaterRoomRepository.save(theaterRoom);
	}

	/**
	 *  Theater
	 */

	public Theater saveTheater(String name) {
		Theater theater = new Theater(Region.SEOUL, name);
		return theaterRepository.save(theater);
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
		return userRepository.save(user);
	}

	/**
	 *  Ticket
	 */

	public Ticket saveTicket() {
		User user = saveUser();
		Schedule schedule = saveSchedule();
		Ticket ticket = new Ticket(user, schedule, LocalDateTime.now());
		return ticketRepository.save(ticket);
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
		return scheduleRepository.save(schedule);
	}

	/**
	 *  Movie
	 */

	public Movie saveMovie(String name) {
		Movie movie = new Movie(name, LimitAge.ADULT, Genre.ACTION, 100);
		return movieRepository.save(movie);
	}

	/**
	 *  ReservedSeat
	 */

	public ReservedSeat saveReservedSeat() {
		Ticket ticket = saveTicket();
		Seat seat = saveSeat();

		ReservedSeat reservedSeat = new ReservedSeat(ticket, seat);
		return reservedSeatRepository.save(reservedSeat);
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
		return ticket.getSchedule();
	}

	/**
	 * 지정한 좌석 리스트를 1개의 티켓에 저장한다.
	 * @param reserveSeatList 예매 할 좌석 리스트
	 * @return Schedule 티켓의 스케줄 정보
	 */
	public Schedule saveReservedSeatMultiSeat(List<Seat> reserveSeatList) {
		Ticket ticket = saveTicket();
		reserveSeatList.forEach((seat) -> {
			ReservedSeat reservedSeat = new ReservedSeat(ticket, seat);
			reservedSeatRepository.save(reservedSeat);
		});
		return ticket.getSchedule();
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

	public void queryCall() {
		em.flush();
	}
}
