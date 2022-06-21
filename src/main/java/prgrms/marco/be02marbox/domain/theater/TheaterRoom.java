package prgrms.marco.be02marbox.domain.theater;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "theater_room")
public class TheaterRoom {
	private static final String SEATS_NOT_EMPTY_ERR = "좌석 정보를 1개이상 등록해야 합니다.";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id")
	@NotNull
	private Theater theater;

	@Column(name = "name")
	private String name;

	@Column(name = "total_seats")
	private Integer totalSeats;

	@OneToMany(mappedBy = "theaterRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	@NotNull
	@Size(min = 1, message = SEATS_NOT_EMPTY_ERR)
	private List<Seat> seats = new ArrayList<>();

	protected TheaterRoom() {
	}

	public TheaterRoom(Theater theater, String name, List<Seat> seats) {
		this.theater = theater;
		this.name = name;
		setSeats(seats);
		this.totalSeats = seats.size();
	}

	private void setSeats(List<Seat> seats) {
		seats.forEach(this::accept);
		this.seats = seats;
	}

	public Long getId() {
		return id;
	}

	public Theater getTheater() {
		return theater;
	}

	public String getName() {
		return name;
	}

	public Integer getTotalSeats() {
		return totalSeats;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		TheaterRoom that = (TheaterRoom)obj;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("name", name)
			.append("totalSeats", totalSeats)
			.toString();
	}

	private void accept(Seat seat) {
		seat.changeTheaterRoom(this);
	}
}
