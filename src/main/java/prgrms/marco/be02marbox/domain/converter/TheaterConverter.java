package prgrms.marco.be02marbox.domain.converter;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;

@Component
public class TheaterConverter {
	public Theater getTheater(RequestCreateTheater request) {
		return new Theater(
			Region.valueOf(request.region()), request.name());
	}
}
