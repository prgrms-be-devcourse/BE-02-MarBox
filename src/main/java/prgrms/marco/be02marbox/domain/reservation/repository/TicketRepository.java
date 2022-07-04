package prgrms.marco.be02marbox.domain.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import prgrms.marco.be02marbox.domain.reservation.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Override
	@Query(
		"select u.name, s.movie.name, s.movie.limitAge, s.theaterRoom.theater.name, s.theaterRoom.name, s.startTime, s.endTime, s.theaterRoom "
			+ "from Ticket t left join  t.user u "
			+ "left join  t.schedule s "
			+ "left join s.movie m "
			+ "left join s.theaterRoom thr "
			+ "left join s.theaterRoom.theater th "
			+ "where t.id = :ticketId"
	)
	Optional<Ticket> findById(@Param("ticketId") Long ticketId);

	@Query(
		"select  u.name, s.movie.name, s.movie.limitAge, s.theaterRoom.theater.name, s.theaterRoom.name, s.startTime, s.endTime "
			+ "from Ticket t left join  t.user u "
			+ "left join  t.schedule s "
			+ "left join s.movie m "
			+ "left join s.theaterRoom thr "
			+ "left join s.theaterRoom.theater th "
			+ "where t.user.id = :userId"
	)
	List<Ticket> findAllTicketByUserId(@Param("userId") Long userId);

	@Query(
		"select u.name, s.movie.name, s.movie.limitAge, s.theaterRoom.theater.name, s.theaterRoom.name, s.startTime, s.endTime "
			+ "from Ticket t left join  t.user u "
			+ "left join  t.schedule s "
			+ "left join s.movie m "
			+ "left join s.theaterRoom thr "
			+ "left join s.theaterRoom.theater th "
			+ "where t.schedule.id = :scheduleId"
	)
	List<Ticket> findAllByScheduleId(@Param("scheduleId") Long scheduleId);
}
