package prgrms.marco.be02marbox.domain.theater.dto;

import javax.validation.constraints.NotNull;

import prgrms.marco.be02marbox.domain.theater.Region;

public record ResponseFindTheater(@NotNull Region region,
	@NotNull String theaterName) {
}
