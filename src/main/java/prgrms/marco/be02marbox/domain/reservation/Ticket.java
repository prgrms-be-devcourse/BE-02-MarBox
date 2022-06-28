package prgrms.marco.be02marbox.domain.reservation;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.user.User;

@Entity
@Table(name = "ticket")
public class Ticket {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;

	@Column(name = "reserved_at")
	private LocalDateTime reservedAt;

	public Ticket() {
	}

	public Ticket(User user, Schedule schedule, LocalDateTime reservedAt) {
		this.user = user;
		this.schedule = schedule;
		this.reservedAt = reservedAt;
	}

	public Schedule getSchedule() {
		return schedule;
	}
}
