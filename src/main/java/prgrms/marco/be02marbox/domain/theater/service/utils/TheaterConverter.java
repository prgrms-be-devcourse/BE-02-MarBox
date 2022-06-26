package prgrms.marco.be02marbox.domain.theater.service.utils;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheater;

@Component
public class TheaterConverter {
	public Theater convertFromRequestCreateTheaterToTheater(RequestCreateTheater request) {
		return new Theater(
			Region.getRegion(request.region()), request.name());
	}

	public ResponseFindTheater convertFromTheaterToResponseFindTheater(Theater theater) {
		return new ResponseFindTheater(theater.getRegion(), theater.getName());
	}
}
