package prgrms.marco.be02marbox.domain.reservation.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.user.User;

public record RequestCreateTicket(@NotNull User user, @NotNull Schedule schedule, @NotNull LocalDateTime reservedAt) {

}
