package prgrms.marco.be02marbox.domain.theater.dto;

import javax.validation.constraints.Min;

public record RequestCreateSeat(@Min(0) int row, @Min(0) int col) {
}
