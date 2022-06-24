package prgrms.marco.be02marbox.domain.exception.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import prgrms.marco.be02marbox.domain.exception.dto.ResponseApiError;

@RestControllerAdvice
public class CommonExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ResponseApiError> handlerBadRequestException(Exception exception) {
		List<String> messages = new ArrayList<>();

		messages.add(exception.getMessage());
		return ResponseEntity.badRequest().body(new ResponseApiError(messages, HttpStatus.BAD_REQUEST.value()));
	}

}
