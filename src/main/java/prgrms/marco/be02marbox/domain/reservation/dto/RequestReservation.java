package prgrms.marco.be02marbox.domain.reservation.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

public record RequestReservation(
	@NotNull Long userId,
	@NotNull Long scheduleId,
	@NotNull List<Long> selectedSeatIds
) {

}
