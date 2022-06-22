package prgrms.marco.be02marbox.domain.movie.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;

public record CreateMovie(@NotBlank String name, @NotNull LimitAge limitAge, @NotNull Genre genre, @NotNull Integer runningTime, @NotBlank String originalFileName) {
}
