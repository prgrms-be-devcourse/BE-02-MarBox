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
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private ServletOutputStream servletOutputStream;

	@Test
	@DisplayName("예외처리 성공")
	void testExceptionHandleSuccess() throws ServletException, IOException {
		//given
		CustomAccessDeniedHandler customAccessDeniedHandler = new CustomAccessDeniedHandler();

		given(this.response.getOutputStream()).willReturn(this.servletOutputStream);

		//when
		customAccessDeniedHandler.handle(
			this.request, this.response, new AccessDeniedException("권한이 없습니다."));

		//then
		InOrder order = inOrder(this.response);
		order.verify(this.response).setContentType(MediaType.APPLICATION_JSON_VALUE);
		order.verify(this.response).setStatus(HttpStatus.FORBIDDEN.value());
		order.verify(this.response).getOutputStream();
	}
}
