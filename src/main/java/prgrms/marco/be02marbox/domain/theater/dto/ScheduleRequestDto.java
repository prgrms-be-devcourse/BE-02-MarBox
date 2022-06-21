package prgrms.marco.be02marbox.domain.theater.dto;

import java.time.LocalDateTime;

public class ScheduleRequestDto {

	private Long theaterRoomId;
	private Long movieId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	public ScheduleRequestDto(Long theaterRoomId, Long movieId, LocalDateTime startTime, LocalDateTime endTime) {
		this.theaterRoomId = theaterRoomId;
		this.movieId = movieId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Long getTheaterRoomId() {
		return theaterRoomId;
	}

	public Long getMovieId() {
		return movieId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}
}
