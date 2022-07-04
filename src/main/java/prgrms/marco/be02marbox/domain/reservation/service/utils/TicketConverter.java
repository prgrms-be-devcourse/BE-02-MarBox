package prgrms.marco.be02marbox.domain.reservation.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;

@Component
public class TicketConverter {

	public ResponseFindTicket convertFromTicketToResponseFindTicket(Ticket ticket) {
		return new ResponseFindTicket(
			ticket.getUser().getName(),
			ticket.getSchedule().getMovie().getName(),
			ticket.getSchedule().getMovie().getLimitAge().name(),
			ticket.getSchedule().getTheaterRoom().getTheater().getName(),
			ticket.getSchedule().getStartTime(),
			ticket.getSchedule().getEndTime(),
			ticket.getSchedule().getTheaterRoom().getName()
		);
	}
}
