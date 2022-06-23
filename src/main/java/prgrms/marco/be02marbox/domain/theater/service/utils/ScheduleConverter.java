package prgrms.marco.be02marbox.domain.theater.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;

@Component
public class ScheduleConverter {

	public Schedule convertFromRequestCreateScheduleToschdeule(RequestCreateSchedule request, TheaterRoom theaterRoom,
		Movie movie) {
		Schedule schedule = Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(request.startTime())
			.endTime(request.endTime())
			.build();

		return schedule;
	}
}
