package prgrms.marco.be02marbox.domain.theater.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.service.TheaterCommonService;

@RestController
@RequestMapping("/theater-rooms")
public class TheaterRoomController {

	private static final String SLASH = "/";

	private final TheaterCommonService theaterCommonService;

	public TheaterRoomController(TheaterCommonService theaterCommonService) {
		this.theaterCommonService = theaterCommonService;
	}

	@PostMapping
	public ResponseEntity<Void> save(HttpServletRequest request,
		@RequestBody @Validated RequestCreateTheaterRoom requestCreateTheaterRoom
	) throws URISyntaxException {
		Long savedId = theaterCommonService.saveTheaterRoomWithSeatList(requestCreateTheaterRoom);
		String redirectUri = new StringBuilder(request.getRequestURI()).append(SLASH).append(savedId).toString();
		return ResponseEntity.created(URI.create(redirectUri)).build();
	}
}
