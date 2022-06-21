package prgrms.marco.be02marbox.domain.movie.service.utils;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.CreateMovieDto;

public class MovieDtoConverter {

	public static Movie convertToMovie(CreateMovieDto createMovieDto) {
		return new Movie(createMovieDto.getName(),
			createMovieDto.getLimitAge(),
			createMovieDto.getGenre(),
			createMovieDto.getRunningTime(),
			createMovieDto.getOriginalFileName());
	}
}
