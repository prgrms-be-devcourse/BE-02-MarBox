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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.domain.exception.dto.ResponseApiError;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final ResponseApiError responseApiError =
		new ResponseApiError(List.of(UN_AUTHORIZED_EXP_MSG.getMessage()), HttpStatus.UNAUTHORIZED.value());

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		log.error("인증되지 않았습니다.", authException);

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		try (OutputStream os = response.getOutputStream()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(os, responseApiError);
			os.flush();
		}
	}
}
