package prgrms.marco.be02marbox.domain.theater.dto;

public record ResponseFindTheaterRoom(ResponseFindTheater theater,
	String theaterRoomName,
	Integer totalCount) {
}
