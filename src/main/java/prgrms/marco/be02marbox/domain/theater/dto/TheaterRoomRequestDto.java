package prgrms.marco.be02marbox.domain.theater.dto;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TheaterRoomRequestDto {
	@NotNull
	private Long theaterId;
	@NotNull
	private String name;
	@NotNull
	@Size(min = 1)
	private List<SeatRequestDto> seatRequestDtos;

	public TheaterRoomRequestDto(Long theaterId, String name,
		List<SeatRequestDto> seatRequestDtos) {
		this.theaterId = theaterId;
		this.name = name;
		this.seatRequestDtos = seatRequestDtos;
	}

	public Long getTheaterId() {
		return theaterId;
	}

	public String getName() {
		return name;
	}

	public List<SeatRequestDto> getSeatRequestDtos() {
		return seatRequestDtos;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		TheaterRoomRequestDto that = (TheaterRoomRequestDto)obj;
		return Objects.equals(theaterId, that.theaterId) && Objects.equals(name, that.name)
			&& seatRequestDtos.equals(that.seatRequestDtos);
	}
}
