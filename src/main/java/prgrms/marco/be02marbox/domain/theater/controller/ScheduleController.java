package prgrms.marco.be02marbox.domain.theater.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindCurrentMovie;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindMovieAndDate;
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
	public ResponseEntity<List<ResponseFindCurrentMovie>> getCurrentMovieList() {
		List<ResponseFindCurrentMovie> currentMovieList = scheduleService.getCurrentMovieList();
		return ResponseEntity.ok().body(currentMovieList);
	}

	@GetMapping
	public ResponseEntity<List<ResponseFindMovieAndDate>> getMovieAndDateListInOneTheater(
		@RequestParam Long theaterId) {
		return ResponseEntity.ok().body(scheduleService.findMovieAndDateWithTheaterId(theaterId));
	}

}
