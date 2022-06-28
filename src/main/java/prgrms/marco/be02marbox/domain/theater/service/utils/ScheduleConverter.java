package prgrms.marco.be02marbox.domain.theater.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindMovieAndDate;

@Component
public class ScheduleConverter {

	public Schedule convertFromRequestCreateScheduleToSchedule(RequestCreateSchedule request, TheaterRoom theaterRoom,
		Movie movie) {
		return Schedule.builder()
			.theaterRoom(theaterRoom)
			.movie(movie)
			.startTime(request.startTime())
			.endTime(request.endTime())
			.build();
	}

	public ResponseFindMovieAndDate convertFromScheduleToResponseFindMovieAndDate(Schedule schedule) {
		return new ResponseFindMovieAndDate(schedule.getMovie().getName(), schedule.getStartTime().toLocalDate());
	}
}
