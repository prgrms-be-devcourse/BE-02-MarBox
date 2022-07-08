package prgrms.marco.be02marbox.domain.theater.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSeat;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;

@Component
public class SeatConverter {

	public Seat convertFromRequestSeatToSeat(TheaterRoom theaterRoom, RequestCreateSeat requestCreateSeat) {
		return new Seat(theaterRoom, requestCreateSeat.row(), requestCreateSeat.col());
	}

	public ResponseFindSeat convertFromSeatToResponseFindSeat(Seat seat) {
		return new ResponseFindSeat(seat.getRow(), seat.getColumn());
	}
}
