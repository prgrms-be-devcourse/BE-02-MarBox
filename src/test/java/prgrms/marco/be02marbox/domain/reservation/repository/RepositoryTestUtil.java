package prgrms.marco.be02marbox.domain.reservation.repository;

import java.time.LocalDateTime;
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

@DataJpaTest
class RepositoryTestUtil {

	@PersistenceContext
	EntityManager em;

	@Autowired
	ReservedSeatRepository reservedSeatRepository;

	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	SeatRepository seatRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TheaterRepository theaterRepository;

	@Autowired
	TheaterRoomRepository theaterRoomRepository;

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	ScheduleRepository scheduleRepository;

	public Seat saveSeat(TheaterRoom theaterRoom, int row, int col) {
		Seat seat = new Seat(theaterRoom, row, col);
		return seatRepository.save(seat);
	}

	public Seat saveSeat() {
		TheaterRoom theaterRoom = saveTheaterRoom("A관");
		Seat seat = new Seat(theaterRoom, 0, 0);
		return seatRepository.save(seat);
	}

	public TheaterRoom saveTheaterRoom(String name) {
		Theater theater = saveTheater("강남");
		TheaterRoom theaterRoom = new TheaterRoom(theater, name);
		return theaterRoomRepository.save(theaterRoom);
	}

	public Theater saveTheater(String name) {
		Theater theater = new Theater(Region.SEOUL, name);
		return theaterRepository.save(theater);
	}

	public User saveUser() {
		User user = new User(
			UUID.randomUUID() + "@email.com",
			"1234",
			"test",
			Role.ROLE_CUSTOMER);
		return userRepository.save(user);
	}

	public Ticket saveTicket() {
		User user = saveUser();
		Schedule schedule = saveSchedule();
		Ticket ticket = new Ticket(user, schedule, LocalDateTime.now());
		return ticketRepository.save(ticket);
	}

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

	public Movie saveMovie(String name) {
		Movie movie = new Movie(name, LimitAge.ADULT, Genre.ACTION, 100, "test");
		return movieRepository.save(movie);
	}

	public ReservedSeat saveReservedSeat() {
		Ticket ticket = saveTicket();
		Seat seat = saveSeat();

		ReservedSeat reservedSeat = new ReservedSeat(ticket, seat);
		return reservedSeatRepository.save(reservedSeat);
	}

	public Schedule saveSameScheduleSeatList(int count) {
		Ticket ticket = saveTicket();
		int maxCount = 100;
		if (count > maxCount) {
			count = maxCount;
		}
		int maxRow = 10;
		TheaterRoom theaterRoom = saveTheaterRoom("A관");
		IntStream.range(0, count).forEach((index) -> {
			int row = index / maxRow;
			int col = index % maxRow;
			Seat seat = saveSeat(theaterRoom, row, col);
			ReservedSeat reservedSeat = new ReservedSeat(ticket, seat);
			reservedSeatRepository.save(reservedSeat);
		});

		return ticket.getSchedule();
	}

	public void queryCall() {
		em.flush();
	}
}
