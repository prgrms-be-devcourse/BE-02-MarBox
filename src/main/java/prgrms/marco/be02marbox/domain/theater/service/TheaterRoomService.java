package prgrms.marco.be02marbox.domain.theater.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheater;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRoomRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterConverter;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterRoomConverter;

@Service
@Transactional(readOnly = true)
public class TheaterRoomService {

	private final TheaterRoomRepository theaterRoomRepository;
	private final TheaterRoomConverter theaterRoomConverter;
	private final TheaterConverter theaterConverter;

	public TheaterRoomService(TheaterRoomRepository theaterRoomRepository,
		TheaterRoomConverter theaterRoomConverter,
		TheaterConverter theaterConverter) {
		this.theaterRoomRepository = theaterRoomRepository;
		this.theaterRoomConverter = theaterRoomConverter;
		this.theaterConverter = theaterConverter;
	}

	/**
	 * 새로운 상영관 추가
	 * @param theaterRoom 저장 될 상영관
	 * @return 생성된 id
	 */
	@Transactional
	public TheaterRoom save(TheaterRoom theaterRoom) {
		return theaterRoomRepository.save(theaterRoom);
	}

	/**
	 * 등록된 모든 상영관 조회
	 * @return 상영관 리스트
	 */
	public List<ResponseFindTheaterRoom> findAll() {
		return theaterRoomRepository.findAll().stream()
			.map(theaterRoom -> {
				ResponseFindTheater responseFindTheater = theaterConverter.convertFromTheaterToResponseFindTheater(
					theaterRoom.getTheater());
				return theaterRoomConverter.convertFromTheaterRoomToResponseFindTheaterRoom(responseFindTheater,
					theaterRoom);
			})
			.collect(toList());
	}
}
