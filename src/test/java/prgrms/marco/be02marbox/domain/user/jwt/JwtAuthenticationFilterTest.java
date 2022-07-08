package prgrms.marco.be02marbox.domain.user.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@Mock
	private MockHttpServletRequest req;

	@Mock
	private MockHttpServletResponse res;

	@Mock
	private MockFilterChain chain;

	@Mock
	private Jwt jwt;

	private JwtAuthenticationFilter filter;

	@BeforeEach
	void setUp() {
		SecurityContextHolder.clearContext();
		this.filter = new JwtAuthenticationFilter("access-token", this.jwt);
	}

	@AfterEach
	void clear() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("JwtAuthenticationFilter 인증 성공")
	void testAuthenticationSuccess() throws ServletException, IOException {
		//given
		String token = "jwt-token";
		given(this.req.getCookies()).willReturn(new Cookie[] {new Cookie("access-token", token)});

		String username = "pang";
		String role = "ROLE_ADMIN";
		given(this.jwt.verify(token)).willReturn(Jwt.Claims.from(username, role));

		//when
		this.filter.doFilter(this.req, this.res, this.chain);

		//then
		verify(this.chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();

		JwtAuthentication jwtAuthentication =
			getPrincipal((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication());
		List<GrantedAuthority> authorities = getAuthorities(SecurityContextHolder.getContext().getAuthentication());

		assertAll(
			() -> assertThat(jwtAuthentication.getToken()).isEqualTo(token),
			() -> assertThat(jwtAuthentication.getUsername()).isEqualTo(username),
			() -> assertThat(authorities.get(0).getAuthority()).isEqualTo(role)
		);
	}

	private JwtAuthentication getPrincipal(JwtAuthenticationToken authentication) {
		return (JwtAuthentication)authentication.getPrincipal();
	}

	private List<GrantedAuthority> getAuthorities(Authentication authentication) {
		return new ArrayList<>(authentication.getAuthorities());
	}

	@Test
	@DisplayName("JwtAuthenticationFilter 인증 실패 - 요청 헤더에 토큰 없음")
	void testAuthenticationFailBecauseEmptyToken() throws ServletException, IOException {
		//given when
		this.filter.doFilter(this.req, this.res, this.chain);

		//then
		verify(this.chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}
}
