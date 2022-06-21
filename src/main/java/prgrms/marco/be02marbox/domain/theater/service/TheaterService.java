package prgrms.marco.be02marbox.domain.theater.service;

import org.springframework.stereotype.Service;

import prgrms.marco.be02marbox.domain.converter.TheaterConverter;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;
import prgrms.marco.be02marbox.domain.theater.repository.TheaterRepository;

@Service
public class TheaterService {

	private final TheaterRepository theaterRepository;
	private final TheaterConverter theaterConverter;

	public TheaterService(TheaterRepository theaterRepository, TheaterConverter theaterConverter) {
		this.theaterRepository = theaterRepository;
		this.theaterConverter = theaterConverter;
	}

	public Long createTheater(RequestCreateTheater request) {
		Theater newTheater = theaterConverter.getTheater(request);
		Theater savedTheater = theaterRepository.save(newTheater);
		return savedTheater.getId();
	}

}