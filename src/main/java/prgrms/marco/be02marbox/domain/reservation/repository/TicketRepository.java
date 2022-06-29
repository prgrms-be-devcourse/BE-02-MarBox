package prgrms.marco.be02marbox.domain.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import prgrms.marco.be02marbox.domain.reservation.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	Optional<Ticket> findTicketByUserId(Long userId);

	@Query("select t "
		+ "from Ticket t "
		+ "join User u on t.user.id = u.id "
		+ "join Schedule s on t.schedule.id = s.id "
		+ "where t.schedule.endTime > current_time ")
	List<Ticket> findAllValidTickets(Long userId);

	@Query("select t from Ticket t inner join Schedule s on t.schedule.id = s.id where s.endTime > current_time and t.user.id = :userId")
	List<Ticket> findAllValidTicketsV2(@Param("userId") Long userId);

}
