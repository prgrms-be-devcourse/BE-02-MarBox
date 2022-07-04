package prgrms.marco.be02marbox.domain.theater.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheater;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheaterRoom;

@Component
public class TheaterRoomConverter {
	public ResponseFindTheaterRoom convertFromTheaterRoomToResponseFindTheaterRoom(
		ResponseFindTheater responseFindTheater, TheaterRoom theaterRoom) {
		return new ResponseFindTheaterRoom(responseFindTheater, theaterRoom.getName(), theaterRoom.getTotalAmount());
	}
}
