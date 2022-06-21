package prgrms.marco.be02marbox.domain.movie.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;

public class CreateMovieDto {

	@NotBlank
	private String name;

	@NotBlank
	private LimitAge limitAge;

	@NotBlank
	private Genre genre;

	@NotNull
	@Min(1)
	private Integer runningTime;

	@NotBlank
	private String originalFileName;

	public CreateMovieDto() {
	}

	public CreateMovieDto(String name, LimitAge limitAge, Genre genre, Integer runningTime, String originalFileName) {
		this.name = name;
		this.limitAge = limitAge;
		this.genre = genre;
		this.runningTime = runningTime;
		this.originalFileName = originalFileName;
	}

	public String getName() {
		return name;
	}

	public LimitAge getLimitAge() {
		return limitAge;
	}

	public Genre getGenre() {
		return genre;
	}

	public Integer getRunningTime() {
		return runningTime;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}
}
