package prgrms.marco.be02marbox.domain.theater.converter;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.dto.SeatRequestDto;

@Component
public class SeatConverter {

	public Seat convertSeat(SeatRequestDto seatRequestDto) {
		return new Seat(seatRequestDto.getRow(), seatRequestDto.getCol());
	}
}
