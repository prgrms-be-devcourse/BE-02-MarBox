package prgrms.marco.be02marbox.domain.user.jwt;

import static org.mockito.BDDMockito.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationEntryPointTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private ServletOutputStream outputStream;

	@Test
	@DisplayName("예외처리 성공")
	void testExceptionHandleSuccess() throws ServletException, IOException {
		//given
		CustomAuthenticationEntryPoint authenticationEntryPoint = new CustomAuthenticationEntryPoint();

		given(this.response.getOutputStream()).willReturn(this.outputStream);

		//when
		authenticationEntryPoint.commence(
			this.request,
			this.response,
			new InsufficientAuthenticationException(""));

		//then
		InOrder order = inOrder(this.response);
		order.verify(this.response).setContentType(MediaType.APPLICATION_JSON_VALUE);
		order.verify(this.response).setStatus(HttpStatus.UNAUTHORIZED.value());
		order.verify(this.response).getOutputStream();
	}
}
