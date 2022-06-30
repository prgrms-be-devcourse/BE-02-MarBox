package prgrms.marco.be02marbox.domain.theater.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record ResponseFindSeat(@NotNull @Min(0) Integer row, @NotNull @Min(0) int col) {
}
