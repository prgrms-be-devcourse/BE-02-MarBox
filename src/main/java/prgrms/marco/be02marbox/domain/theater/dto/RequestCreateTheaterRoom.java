package prgrms.marco.be02marbox.domain.theater.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record RequestCreateTheaterRoom(@NotNull Long theaterId,
	@NotNull String name,
	@NotNull @Size(min = 1) Set<RequestCreateSeat> requestCreateSeats) {
}
