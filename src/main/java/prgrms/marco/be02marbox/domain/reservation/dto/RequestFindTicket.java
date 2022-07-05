package prgrms.marco.be02marbox.domain.reservation.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

public record RequestFindTicket(@NotNull Long ticketId, @NotNull List<Long> reservedSeatIds) {
}
