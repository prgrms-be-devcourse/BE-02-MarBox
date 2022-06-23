package prgrms.marco.be02marbox.domain.theater.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;

@Component
public class TheaterConverter {
	public Theater convertFromRequestCreateTheaterToTheater(RequestCreateTheater request) {
		return new Theater(
			Region.valueOf(request.region()), request.name());
	}
}
