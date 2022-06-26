package prgrms.marco.be02marbox.domain.movie.dto;

import java.util.List;

import prgrms.marco.be02marbox.domain.movie.Movie;

public record ResponseGetMovies(List<Movie> movies) {
}
