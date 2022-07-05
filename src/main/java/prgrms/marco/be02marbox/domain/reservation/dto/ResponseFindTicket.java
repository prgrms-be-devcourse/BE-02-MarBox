package prgrms.marco.be02marbox.domain.reservation.dto;

import java.time.LocalDateTime;
import java.util.List;

import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;

public record ResponseFindTicket(
	String username,
	String movieName,
	LimitAge limitAge,
	String theaterName,
	String theaterRoomName,
	LocalDateTime startTime,
	LocalDateTime endTime,
	List<ResponseFindSeat> seat
) {

}
