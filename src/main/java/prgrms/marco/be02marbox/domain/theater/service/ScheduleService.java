package prgrms.marco.be02marbox.domain.theater.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.ScheduleConverter;

@Service
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final ScheduleConverter converter;

	public ScheduleService(ScheduleRepository scheduleRepository,
		ScheduleConverter converter) {
		this.scheduleRepository = scheduleRepository;
		this.converter = converter;
	}

	@Transactional
	public Long createSchedule(RequestCreateSchedule requestCreateSchedule) {
		Schedule schedule = converter.convertFromRequestCreateScheduleToSchdeule(requestCreateSchedule);

		Schedule savedSchedule = scheduleRepository.save(schedule);

		return savedSchedule.getId();
	}

}
