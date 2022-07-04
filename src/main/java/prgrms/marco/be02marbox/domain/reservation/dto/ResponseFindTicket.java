package prgrms.marco.be02marbox.domain.reservation.dto;

import java.time.LocalDateTime;

public record ResponseFindTicket(
	String username,
	String movieName,
	String limitAge,
	String theaterName,
	LocalDateTime startTime,
	LocalDateTime endTime,
	String theaterRoomName) {

}
