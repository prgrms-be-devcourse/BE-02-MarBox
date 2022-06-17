package prgrms.marco.be02marbox.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reserved_seat")
public class ReservedSeat {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id")
	private Ticket ticket;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id")
	private Seat seat;
}
