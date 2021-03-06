package prgrms.marco.be02marbox.domain.reservation.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

public record RequestCreateTicket(
	@NotNull Long userId,
	@NotNull Long scheduleId,
	@NotNull LocalDateTime reservedAt,
	@NotNull List<Long> selectedSeatIds) {
}
