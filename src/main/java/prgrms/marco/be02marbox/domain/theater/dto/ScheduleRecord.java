package prgrms.marco.be02marbox.domain.theater.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

public record ScheduleRecord(
	@NotNull Long theaterRoomId,
	@NotNull Long movieId,
	@NotNull LocalDateTime startTime,
	@NotNull LocalDateTime endTime) {

}
