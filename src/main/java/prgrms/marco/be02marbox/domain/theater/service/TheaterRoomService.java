package prgrms.marco.be02marbox.domain.theater.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.SeatConverter;

@Service
public class TheaterRoomService {

	private static final String NOT_FOUND_THEATER_ERR = "극장 정보를 조회할 수 없습니다.";

	private final TheaterRoomRepository theaterRoomRepository;
	private final TheaterRepository theaterRepository;
	private final SeatConverter seatConverter;

	public TheaterRoomService(TheaterRoomRepository theaterRoomRepository,
		TheaterRepository theaterRepository,
		SeatConverter seatConverter) {
		this.theaterRoomRepository = theaterRoomRepository;
		this.theaterRepository = theaterRepository;
		this.seatConverter = seatConverter;
	}

	@Transactional
	public Long save(RequestCreateTheaterRoom requestCreateTheaterRoom) {
		Theater theater = theaterRepository.findById(requestCreateTheaterRoom.theaterId())
			.orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_THEATER_ERR));

		TheaterRoom newTheaterRoom = new TheaterRoom(theater, requestCreateTheaterRoom.name());
		TheaterRoom savedTheaterRoom = theaterRoomRepository.save(newTheaterRoom);
		List<Seat> seatList = requestCreateTheaterRoom.requestCreateSeats().stream().map(requestCreateSeat ->
			seatConverter.convertFromRequestSeatToSeat(savedTheaterRoom, requestCreateSeat)
		).collect(toList());
		savedTheaterRoom.addSeats(seatList);
		return savedTheaterRoom.getId();
	}
}
