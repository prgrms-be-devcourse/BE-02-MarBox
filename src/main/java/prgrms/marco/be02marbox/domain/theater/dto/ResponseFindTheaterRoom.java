package prgrms.marco.be02marbox.domain.theater.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record ResponseFindTheaterRoom(ResponseFindTheater theater,
	@NotNull String theaterRoomName,
	@NotNull @Min(0) Integer totalCount) {
}
