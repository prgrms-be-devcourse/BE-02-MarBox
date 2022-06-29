package prgrms.marco.be02marbox.domain.movie.controller.api;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.movie.dto.RequestCreateMovie;
import prgrms.marco.be02marbox.domain.movie.dto.ResponseCreateMovie;
import prgrms.marco.be02marbox.domain.movie.dto.ResponseGetMovies;
import prgrms.marco.be02marbox.domain.movie.service.MovieService;

@RequestMapping("/api/movies")
@RestController
public class MovieRestController {

	private static final String DEFAULT_PAGE = "0";
	private static final String DEFAULT_SIZE = "10";

	private final MovieService movieService;

	public MovieRestController(MovieService movieService) {
		this.movieService = movieService;
	}

	@PostMapping
	public ResponseEntity<ResponseCreateMovie> createMovie(
		@Validated @ModelAttribute RequestCreateMovie requestCreateMovie) throws IOException {
		return ResponseEntity
			.ok()
			.body(new ResponseCreateMovie(movieService.createMovie(requestCreateMovie)));

	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseGetMovies> getMovies(
		@RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
		@RequestParam(name = "size", defaultValue = DEFAULT_SIZE) int size) {
		return ResponseEntity
			.ok()
			.body(new ResponseGetMovies(movieService.getMovies(page, size)));
	}
}
