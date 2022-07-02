package prgrms.marco.be02marbox.domain.reservation.service.utils;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.user.User;

@Component
public class TicketConverter {

	public ResponseFindTicket convertFromTicketToResponseFindTicket(Ticket ticket) {
		return new ResponseFindTicket(ticket.getUser(), ticket.getSchedule(), ticket.getReservedAt());
	}

	public Ticket convertFromRequestCreateTicketToTicket(User user, Schedule schedule, LocalDateTime reservedAt) {
		return new Ticket(user, schedule, reservedAt);
	}
}
