package prgrms.marco.be02marbox.domain.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.reservation.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findAllTicketByUserId(Long userId);
}
