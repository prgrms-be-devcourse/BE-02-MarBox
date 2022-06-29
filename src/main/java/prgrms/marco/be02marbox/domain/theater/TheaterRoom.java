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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "theater_room")
public class TheaterRoom {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id")
	@NotNull
	private Theater theater;

	@Column(name = "name", nullable = false)
	@NotNull
	private String name;

	@OneToMany(mappedBy = "theaterRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	@NotNull
	private List<Seat> seats = new ArrayList<>();

	@Formula("(select count(*) from seat s where s.theater_room_id = id)")
	private int totalCount;

	public List<Seat> getSeats() {
		return seats;
	}

	public void addSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public int getTotalAmount() {
		return this.totalCount;
	}

	protected TheaterRoom() {
	}

	public TheaterRoom(Theater theater, String name) {
		this.theater = theater;
		this.name = name;
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
			.toString();
	}

	public static TheaterRoomBuilder builder() {
		return new TheaterRoomBuilder();
	}

	public static final class TheaterRoomBuilder {
		private Long id;
		private Theater theater;
		private String name;

		private TheaterRoomBuilder() {
		}

		public TheaterRoomBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public TheaterRoomBuilder theater(Theater theater) {
			this.theater = theater;
			return this;
		}

		public TheaterRoomBuilder name(String name) {
			this.name = name;
			return this;
		}

		public TheaterRoom build() {
			TheaterRoom theaterRoom = new TheaterRoom(theater, name);
			theaterRoom.id = this.id;
			return theaterRoom;
		}
	}
}
