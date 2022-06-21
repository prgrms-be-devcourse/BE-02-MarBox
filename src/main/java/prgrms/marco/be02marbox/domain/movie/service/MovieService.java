package prgrms.marco.be02marbox.domain.movie.service;

import org.springframework.stereotype.Service;

import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.movie.dto.CreateMovieDto;
import prgrms.marco.be02marbox.domain.movie.repository.MovieRepository;
import prgrms.marco.be02marbox.domain.movie.service.utils.MovieDtoConverter;

@Service
public class MovieService {

	private final MovieRepository movieRepository;

	public MovieService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public Movie createMovie(CreateMovieDto createMovieDto) {
		Movie newMovie = MovieDtoConverter.convertCreateMovieDto(createMovieDto);
		Movie savedMovie = movieRepository.save(newMovie);
		return savedMovie;
	}
}
