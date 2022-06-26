package prgrms.marco.be02marbox.domain.theater.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTheater;
import prgrms.marco.be02marbox.domain.theater.service.TheaterService;

@RestController
@RequestMapping("/theaters")
public class TheaterController {

	private final TheaterService theaterService;

	public TheaterController(TheaterService theaterService) {
		this.theaterService = theaterService;
	}

	@GetMapping
	public ResponseEntity<List<ResponseFindTheater>> getTheaters() {
		List<ResponseFindTheater> response = theaterService.findTheaters();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<ResponseFindTheater> saveTheater(@RequestBody @Valid RequestCreateTheater request) {
		Long savedTheaterId = theaterService.createTheater(request);
		ResponseFindTheater savedTheater = theaterService.findTheater(savedTheaterId);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTheater);
	}

	@GetMapping("/{theaterId}")
	public ResponseEntity<ResponseFindTheater> getTheater(@PathVariable Long theaterId) {
		ResponseFindTheater findTheater = theaterService.findTheater(theaterId);
		return ResponseEntity.status(HttpStatus.OK).body(findTheater);
	}

	@GetMapping("/region")
	public ResponseEntity<List<ResponseFindTheater>> getTheaterByRegion(@RequestParam("region") String region) {
		List<ResponseFindTheater> theaterByRegion = theaterService.findTheaterByRegion(region);
		return ResponseEntity.status(HttpStatus.OK).body(theaterByRegion);
	}
}
