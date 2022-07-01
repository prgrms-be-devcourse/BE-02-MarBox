package prgrms.marco.be02marbox.domain.reservation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Persistable;

import prgrms.marco.be02marbox.domain.theater.Seat;

@Entity
@Table(name = "reserved_seat")
public class ReservedSeat implements Persistable<String> {

	public static final String ID_SEPARATOR = "_";

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id", nullable = false)
	@NotNull
	private Ticket ticket;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id", nullable = false)
	@NotNull
	private Seat seat;

	@Transient
	private boolean isNew = true;

	protected ReservedSeat() {
	}

	private void makeReservedSeatId(Long scheduleId, Long seatId) {
		this.id = new StringBuilder()
			.append(scheduleId)
			.append(ID_SEPARATOR)
			.append(seatId)
			.toString();
	}

	public ReservedSeat(Ticket ticket, Seat seat) {
		makeReservedSeatId(ticket.getSchedule().getId(), seat.getId());
		this.ticket = ticket;
		this.seat = seat;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}

	@PrePersist
	@PostLoad
	void markNotNew() {
		this.isNew = false;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public Seat getSeat() {
		return seat;
	}

}
