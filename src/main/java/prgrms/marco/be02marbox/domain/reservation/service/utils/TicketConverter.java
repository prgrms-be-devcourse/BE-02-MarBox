package prgrms.marco.be02marbox.domain.reservation.service.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;

@Component
public class TicketConverter {

	public ResponseFindTicket convertFromTicketToResponseFindTicket(Ticket ticket, List<ReservedSeat> reservedSeats) {

		return new ResponseFindTicket(
			ticket.getUser().getName(),
			ticket.getSchedule().getMovie().getName(),
			ticket.getSchedule().getMovie().getLimitAge(),
			ticket.getSchedule().getTheaterRoom().getTheater().getName(),
			ticket.getSchedule().getTheaterRoom().getName(),
			ticket.getSchedule().getStartTime(),
			ticket.getSchedule().getEndTime(),
			reservedSeats.stream()
				.map(rs -> new ResponseFindSeat(rs.getSeat().getRow(), rs.getSeat().getColumn()))
				.collect(Collectors.toList())
		);
	}
}
