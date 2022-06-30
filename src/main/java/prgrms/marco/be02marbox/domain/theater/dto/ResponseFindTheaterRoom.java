package prgrms.marco.be02marbox.domain.theater.dto;

import javax.validation.constraints.NotNull;

public record ResponseFindTheaterRoom(ResponseFindTheater theater,
	@NotNull String theaterRoomName,
	@NotNull Integer totalCount) {
}
