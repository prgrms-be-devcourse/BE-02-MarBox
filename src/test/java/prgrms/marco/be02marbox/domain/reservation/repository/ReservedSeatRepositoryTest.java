package prgrms.marco.be02marbox.domain.reservation.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class ReservedSeatRepositoryTest {

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

	@PersistenceContext
	EntityManager em;

	private Ticket ticket;
	private Seat seat;

	@BeforeEach
	void init() {
		// user
		User user = new User(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_CUSTOMER);
		userRepository.save(user);

		// theater
		Theater theater = new Theater(Region.SEOUL, "강남");
		theaterRepository.save(theater);

		// theaterRoom
		TheaterRoom theaterRoom = new TheaterRoom(theater, "A관");
		theaterRoomRepository.save(theaterRoom);
		em.flush();

		// seat
		seat = new Seat(theaterRoom, 0, 0);
		seatRepository.save(seat);

		// movie
		Movie movie = new Movie("test", LimitAge.ADULT, Genre.ACTION, 100, "test");
		movieRepository.save(movie);

		// schedule
		Schedule schedule = Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now())
			.build();
		scheduleRepository.save(schedule);

		// ticket
		ticket = new Ticket(user, schedule, LocalDateTime.now());
		ticketRepository.save(ticket);

		em.flush();
	}

	@Test
	@DisplayName("scheduleId와 seatId로 아이디를 생성한다.")
	void testMakeReservedSeatId() {
		ReservedSeat reservedSeat = new ReservedSeat(ticket, seat);

		ReservedSeat save = reservedSeatRepository.save(reservedSeat);
		Optional<ReservedSeat> findReservedSeat = reservedSeatRepository.findById(save.getId());

		assertAll(
			() -> assertThat(findReservedSeat).isPresent(),
			() -> {
				ReservedSeat getReservedSeat = findReservedSeat.get();
				StringBuilder makeId = new StringBuilder()
					.append(ticket.getSchedule().getId())
					.append("_")
					.append(seat.getId());
				assertThat(getReservedSeat.getId()).isEqualTo(makeId.toString());
			}
		);
	}
}
