package prgrms.marco.be02marbox.domain.theater.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.converter.SeatConverter;
import prgrms.marco.be02marbox.domain.theater.dto.TheaterRoomRequestDto;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;

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

	public Long save(TheaterRoomRequestDto requestDto) {
		Theater theater = theaterRepository.findById(requestDto.getTheaterId())
			.orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_THEATER_ERR));

		List<Seat> seats = requestDto.getSeatRequestDtos().stream()
			.distinct()
			.map(seatConverter::convertSeat)
			.collect(toList());

		TheaterRoom newTheaterRoom = new TheaterRoom(theater, requestDto.getName(), seats);
		TheaterRoom savedTheaterRoom = theaterRoomRepository.save(newTheaterRoom);
		return savedTheaterRoom.getId();
	}
}
