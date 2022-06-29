package prgrms.marco.be02marbox.domain.exception.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import prgrms.marco.be02marbox.domain.exception.custom.theater.TheaterException;
import prgrms.marco.be02marbox.domain.exception.dto.ResponseApiError;

@RestControllerAdvice(basePackages = "prgrms.marco.be02marbox.domain.theater.controller")
public class TheaterExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResponseApiError> handleIllegalArgument(IllegalArgumentException error) {
		List<String> messages = new ArrayList<>();

		messages.add(error.getMessage());
		return ResponseEntity.badRequest().body(new ResponseApiError(messages, HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ResponseApiError> handlerBadRequestException(EntityNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ResponseApiError(List.of(exception.getMessage()), HttpStatus.NOT_FOUND.value()));
	}

	@ExceptionHandler(TheaterException.class)
	public ResponseEntity<ResponseApiError> handlerTheaterException(TheaterException exception) {
		List<String> message = new ArrayList<>();
		message.add(exception.getMessage());
		return ResponseEntity.status(exception.getStatusCode())
			.body(new ResponseApiError(message, exception.getStatusCode()));
	}
}
