package prgrms.marco.be02marbox.domain.theater.dto;

import javax.validation.constraints.NotNull;

public record ResponseFindTheaterRoom(ResponseFindTheater responseFindTheater,
	@NotNull String theaterRoomName,
	@NotNull int totalCount) {
}
