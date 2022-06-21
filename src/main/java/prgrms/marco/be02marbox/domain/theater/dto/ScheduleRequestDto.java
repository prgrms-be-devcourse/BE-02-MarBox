package prgrms.marco.be02marbox.domain.theater.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

public class ScheduleRequestDto {

	@NotNull
	private Long theaterRoomId;

	@NotNull
	private Long movieId;

	@NotNull
	private LocalDateTime startTime;

	@NotNull
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
