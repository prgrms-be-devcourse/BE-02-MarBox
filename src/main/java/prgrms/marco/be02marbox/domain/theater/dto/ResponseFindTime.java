package prgrms.marco.be02marbox.domain.theater.dto;

import java.time.LocalTime;
import java.util.List;

public record ResponseFindTime(
	String theaterRoomName,
	int totalSeatCount,
	List<LocalTime> startTimeList
) {
}
