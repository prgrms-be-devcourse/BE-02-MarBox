package prgrms.marco.be02marbox.domain.theater.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindMovie;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSchedule;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

	private final ScheduleService scheduleService;

	public ScheduleController(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	@PostMapping
	public ResponseEntity<Void> createSchedule(@Valid @RequestBody RequestCreateSchedule request) throws
		URISyntaxException {
		Long scheduleId = scheduleService.createSchedule(request);
		return ResponseEntity.created(new URI("schedules/" + scheduleId)).build();
	}

	@GetMapping("/current-movies")
	public ResponseEntity<List<ResponseFindMovie>> getCurrentMovieList() {
		List<ResponseFindMovie> currentMovieList = scheduleService.findShowingMovieList();
		return ResponseEntity.ok().body(currentMovieList);
	}

	@GetMapping("/search")
	public ResponseEntity<ResponseFindSchedule> searchSchedule() {
		return ResponseEntity.ok()
			.body(new ResponseFindSchedule(
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList()));
	}

	@GetMapping(value = "/search", params = "theaterId")
	public ResponseEntity<ResponseFindSchedule> searchSchedule(@RequestParam Long theaterId) {
		ResponseFindSchedule movieAndDateList = scheduleService.findMovieListAndDateListByTheaterId(theaterId);
		return ResponseEntity.ok().body(movieAndDateList);
	}

	@GetMapping(value = "/search", params = {"theaterId", "date"})
	public ResponseEntity<ResponseFindSchedule> searchSchedule(
		@RequestParam Long theaterId,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		ResponseFindSchedule movieAndDateList = scheduleService.findMovieListByTheaterIdAndDate(theaterId, date);
		return ResponseEntity.ok().body(movieAndDateList);
	}

	@GetMapping(value = "/search", params = {"movieId", "theaterId", "date"})
	public ResponseEntity<ResponseFindSchedule> searchSchedule(
		@RequestParam Long movieId,
		@RequestParam Long theaterId,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		ResponseFindSchedule movieAndDateList = scheduleService.findTimeScheduleList(movieId, theaterId, date);
		return ResponseEntity.ok().body(movieAndDateList);
	}

}
