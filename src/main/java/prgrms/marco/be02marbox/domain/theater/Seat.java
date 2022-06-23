package prgrms.marco.be02marbox.domain.theater;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "seat", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"theater_room_id", "rows", "columns"})
})
public class Seat {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_room_id")
	private TheaterRoom theaterRoom;

	@Column(name = "rows")
	private Integer row;

	@Column(name = "columns")
	private Integer column;

	protected Seat() {
	}

	public Seat(TheaterRoom theaterRoom, Integer row, Integer column) {
		this.theaterRoom = theaterRoom;
		this.row = row;
		this.column = column;
	}

	public Long getId() {
		return id;
	}

	public TheaterRoom getTheaterRoom() {
		return theaterRoom;
	}

	public Integer getRow() {
		return row;
	}

	public Integer getColumn() {
		return column;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Seat seat = (Seat)obj;
		return Objects.equals(theaterRoom.getId(), seat.theaterRoom) && Objects.equals(row, seat.row)
			&& Objects.equals(column, seat.column);
	}

	@Override
	public int hashCode() {
		return Objects.hash(theaterRoom.getId(), row, column);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("row", row)
			.append("column", column)
			.toString();
	}
}
