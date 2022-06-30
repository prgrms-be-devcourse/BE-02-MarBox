package prgrms.marco.be02marbox.domain.theater.dto;

import java.time.LocalTime;

import prgrms.marco.be02marbox.domain.theater.TheaterRoom;

public record ResponseFindTime(
	LocalTime startTime,
	LocalTime endTime,
	TheaterRoom theaterRoom
) {
}
