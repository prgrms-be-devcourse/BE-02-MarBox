package prgrms.marco.be02marbox.domain.theater.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
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

}
