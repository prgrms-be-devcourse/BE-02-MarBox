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
		"select t "
			+ "from Ticket t left join fetch  t.user u "
			+ "left join fetch  t.schedule s "
			+ "left join fetch s.movie m "
			+ "left join fetch s.theaterRoom thr "
			+ "left join fetch thr.theater th "
			+ "where t.id = :ticketId"
	)
	Optional<Ticket> findById(@Param("ticketId") Long ticketId);

	@Override
	@Query(
		"select t "
			+ "from Ticket t left join fetch  t.user u "
			+ "left join fetch  t.schedule s "
			+ "left join fetch s.movie m "
			+ "left join fetch s.theaterRoom thr "
			+ "left join fetch thr.theater th "
	)
	List<Ticket> findAll();

	@Query(
		"select  t "
			+ "from Ticket t left join fetch t.user u "
			+ "left join fetch t.schedule s "
			+ "left join fetch s.movie m "
			+ "left join fetch s.theaterRoom thr "
			+ "left join fetch thr.theater th "
			+ "where t.user.id = :userId"
	)
	List<Ticket> findAllTicketByUserId(@Param("userId") Long userId);

	@Query(
		"select t "
			+ "from Ticket t left join fetch t.user u "
			+ "left join  fetch t.schedule s "
			+ "left join fetch s.movie m "
			+ "left join fetch s.theaterRoom thr "
			+ "left join fetch thr.theater th "
			+ "where t.schedule.id = :scheduleId"
	)
	List<Ticket> findAllByScheduleId(@Param("scheduleId") Long scheduleId);
}
