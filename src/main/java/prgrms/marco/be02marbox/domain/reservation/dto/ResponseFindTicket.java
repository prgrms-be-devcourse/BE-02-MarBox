package prgrms.marco.be02marbox.domain.reservation.dto;

import java.time.LocalDateTime;

import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.user.User;

public record ResponseFindTicket(User user, Schedule schedule, LocalDateTime reservedAt) {

}
