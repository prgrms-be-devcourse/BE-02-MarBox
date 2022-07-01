package prgrms.marco.be02marbox.domain.theater.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.exception.custom.theater.DuplicateTheaterNameException;
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheater;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterConverter;

@Service
@Transactional(readOnly = true)
public class TheaterService {

	private static final String NOT_FOUND_THEATER_ERR = "극장 정보를 조회할 수 없습니다.";

	private final TheaterRepository theaterRepository;
	private final TheaterConverter theaterConverter;

	public TheaterService(TheaterRepository theaterRepository, TheaterConverter theaterConverter) {
		this.theaterRepository = theaterRepository;
		this.theaterConverter = theaterConverter;
	}

	/**
	 * 관리자가 영화관을 추가하는 메서드
	 * @param request 지역, 영화관 이름
	 * @return 생성된 영화관 ID
	 * @throws DuplicateTheaterNameException 영화관 이름이 중복되는 경우
	 */

	@Transactional
	public Long createTheater(RequestCreateTheater request) {
		theaterRepository.findByName(request.name())
			.ifPresent(theater -> {
				throw new DuplicateTheaterNameException();
			});
		Theater newTheater = theaterConverter.convertFromRequestCreateTheaterToTheater(request);
		Theater savedTheater = theaterRepository.save(newTheater);
		return savedTheater.getId();
	}

	/**
	 * 고객과 관리자가 특정 영화관을 찾는 메서드
	 * @param id 영화관 ID
	 * @return 특정 영화관 지역, 영화관 이름
	 * @throws EntityNotFoundException 영화관이 존재하지 않는 경우
	 */

	public ResponseFindTheater findTheater(Long id) {
		Theater findTheater = theaterRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_THEATER_ERR));

		return theaterConverter.convertFromTheaterToResponseFindTheater(findTheater);
	}

	/**
	 * 고객, 관리자가 전체 영화관을 조회하는 메서드
	 * @return 전체 영화관 리스트
	 */

	public List<ResponseFindTheater> findTheaters() {
		return theaterRepository.findAll()
			.stream()
			.map(theaterConverter::convertFromTheaterToResponseFindTheater)
			.collect(toList());
	}

	/**
	 * 고객, 관리자가 특정 지역의 영화관들을 조회하는 메서드
	 * @param region 지역이름
	 * @return 특정 지역의 전체 영화관 리스트
	 */
	public List<ResponseFindTheater> findTheaterByRegion(String region) {
		return theaterRepository.findAllByRegion(Region.from(region))
			.stream()
			.map(theaterConverter::convertFromTheaterToResponseFindTheater)
			.collect(toList());
	}
}
