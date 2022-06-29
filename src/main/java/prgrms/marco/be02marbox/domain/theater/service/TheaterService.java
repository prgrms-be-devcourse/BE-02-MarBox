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

	public ResponseFindTheater findTheater(Long id) {
		Theater findTheater = theaterRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_THEATER_ERR));

		return theaterConverter.convertFromTheaterToResponseFindTheater(findTheater);
	}

	public List<ResponseFindTheater> findTheaters() {
		return theaterRepository.findAll()
			.stream()
			.map(theaterConverter::convertFromTheaterToResponseFindTheater)
			.collect(toList());
	}

	public List<ResponseFindTheater> findTheaterByRegion(String region) {
		return theaterRepository.findAllByRegion(Region.from(region))
			.stream()
			.map(theaterConverter::convertFromTheaterToResponseFindTheater)
			.collect(toList());
	}
}
