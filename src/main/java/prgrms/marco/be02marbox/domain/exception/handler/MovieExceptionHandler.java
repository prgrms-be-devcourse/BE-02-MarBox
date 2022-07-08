package prgrms.marco.be02marbox.domain.exception.handler;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import prgrms.marco.be02marbox.domain.exception.dto.ResponseApiError;
import prgrms.marco.be02marbox.domain.movie.controller.api.MovieRestController;

@RestControllerAdvice(assignableTypes = MovieRestController.class)
public class MovieExceptionHandler {

	@ExceptionHandler(IOException.class)
	public ResponseEntity<ResponseApiError> handleUserException(Exception exception) {
		return ResponseEntity
			.status(INTERNAL_SERVER_ERROR)
			.body(new ResponseApiError(List.of(exception.getMessage()), INTERNAL_SERVER_ERROR.value()));
	}

}
