package prgrms.marco.be02marbox.domain.user.jwt;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.domain.exception.dto.ResponseApiError;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private static final ResponseApiError responseApiError =
		new ResponseApiError(List.of(ACCESS_DENIED_EXP_MSG.getMessage()), HttpStatus.FORBIDDEN.value());

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {

		log.error("접근 권한이 없는 API에 접근했습니다.", accessDeniedException);

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		try (OutputStream os = response.getOutputStream()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(os, responseApiError);
			os.flush();
		}
	}
}
