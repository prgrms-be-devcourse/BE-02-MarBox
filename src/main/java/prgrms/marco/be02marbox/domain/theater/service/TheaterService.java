package prgrms.marco.be02marbox.domain.theater.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheater;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.TheaterConverter;

@Service
@Transactional(readOnly = true)
public class TheaterService {

	private final TheaterRepository theaterRepository;
	private final TheaterConverter theaterConverter;

	public TheaterService(TheaterRepository theaterRepository, TheaterConverter theaterConverter) {
		this.theaterRepository = theaterRepository;
		this.theaterConverter = theaterConverter;
	}

	@Transactional
	public Long createTheater(RequestCreateTheater request) {
		Theater newTheater = theaterConverter.convertFromRequestCreateTheaterToTheater(request);
		Theater savedTheater = theaterRepository.save(newTheater);
		return savedTheater.getId();
	}

	public List<ResponseFindTheater> findTheaters() {
		return theaterRepository.findAll()
			.stream()
			.map(theaterConverter::convertFromTheaterToResponseFindTheater)
			.collect(toList());
	}

	public List<ResponseFindTheater> findTheaterByRegion(String region) {
		return theaterRepository.findByRegion(Region.valueOf(region.toUpperCase()))
			.stream()
			.map(theaterConverter::convertFromTheaterToResponseFindTheater)
			.collect(toList());
	}
}
