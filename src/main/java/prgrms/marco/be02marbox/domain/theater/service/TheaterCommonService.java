package prgrms.marco.be02marbox.domain.theater.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.service.utils.SeatConverter;

@Service
@Transactional(readOnly = true)
public class TheaterCommonService {

	private final TheaterService theaterService;
	private final TheaterRoomService theaterRoomService;
	private final SeatConverter seatConverter;

	public TheaterCommonService(TheaterService theaterService,
		TheaterRoomService theaterRoomService,
		SeatConverter seatConverter) {
		this.theaterService = theaterService;
		this.theaterRoomService = theaterRoomService;
		this.seatConverter = seatConverter;
	}

	/**
	 * 새로운 상영관 추가
	 * @param requestCreateTheaterRoom 극장, 상영관 이름, 좌석 정보
	 * @return 상영관 id
	 */
	@Transactional
	public Long saveTheaterRoomWithSeatList(RequestCreateTheaterRoom requestCreateTheaterRoom) {
		Theater theater = theaterService.findById(requestCreateTheaterRoom.theaterId());

		TheaterRoom newTheaterRoom = new TheaterRoom(theater, requestCreateTheaterRoom.name());
		TheaterRoom savedTheaterRoom = theaterRoomService.save(newTheaterRoom);
		List<Seat> seatList = requestCreateTheaterRoom.requestCreateSeats().stream().map(requestCreateSeat ->
			seatConverter.convertFromRequestSeatToSeat(savedTheaterRoom, requestCreateSeat)
		).collect(toList());
		savedTheaterRoom.addSeats(seatList);
		return savedTheaterRoom.getId();
	}

}
