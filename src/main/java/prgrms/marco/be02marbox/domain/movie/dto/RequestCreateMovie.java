package prgrms.marco.be02marbox.domain.movie.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;

public record RequestCreateMovie(
	@NotBlank String name,
	@NotNull LimitAge limitAge,
	@NotNull Genre genre,
	@NotNull Integer runningTime,
	@NotNull MultipartFile poster
) {
}
