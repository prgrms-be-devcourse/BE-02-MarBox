package prgrms.marco.be02marbox.domain.exception.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import prgrms.marco.be02marbox.domain.exception.dto.ResponseApiError;

@RestControllerAdvice(basePackages = "prgrms.marco.be02marbox.domain.theater.controller")
public class ScheduleExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResponseApiError> illegalArgumentHandle(Exception error) {
		return ResponseEntity.badRequest()
			.body(new ResponseApiError(List.of(error.getMessage()), HttpStatus.BAD_REQUEST.value()));
	}

}
