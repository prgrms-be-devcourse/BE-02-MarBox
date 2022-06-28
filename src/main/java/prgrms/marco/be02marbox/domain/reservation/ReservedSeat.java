package prgrms.marco.be02marbox.domain.reservation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import prgrms.marco.be02marbox.domain.theater.Seat;

@Entity
@Table(name = "reserved_seat")
public class ReservedSeat {

	private static final String ID_SEPARATOR = "_";

	@Id
	@Column(name = "id", nullable = false)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id", nullable = false)
	@NotNull
	private Ticket ticket;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id", nullable = false)
	@NotNull
	private Seat seat;

	protected ReservedSeat() {
	}

	private void makeReservedSeatId(Long scheduleId, Long seatId) {
		this.id = scheduleId + ID_SEPARATOR + seatId;
	}

	public ReservedSeat(Ticket ticket, Seat seat) {
		makeReservedSeatId(ticket.getSchedule().getId(), seat.getId());
		this.ticket = ticket;
		this.seat = seat;
	}

	public String getId() {
		return id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public Seat getSeat() {
		return seat;
	}

}
