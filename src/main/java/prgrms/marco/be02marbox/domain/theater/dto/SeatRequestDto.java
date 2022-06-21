package prgrms.marco.be02marbox.domain.theater.dto;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SeatRequestDto {
	private int row;
	private int col;

	public SeatRequestDto(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		SeatRequestDto that = (SeatRequestDto)obj;
		return row == that.row && col == that.col;
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, col);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("row", row)
			.append("col", col)
			.toString();
	}
}
