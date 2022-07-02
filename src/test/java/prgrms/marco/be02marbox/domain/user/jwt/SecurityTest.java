package prgrms.marco.be02marbox.domain.user.jwt;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.config.JwtConfigure;
import prgrms.marco.be02marbox.domain.theater.controller.TheaterController;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheater;
import prgrms.marco.be02marbox.domain.theater.service.TheaterService;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@WebMvcTest(TheaterController.class)
@EnableConfigurationProperties(JwtConfigure.class)
class SecurityTest {

	@Autowired
	private Jwt jwt;

	@Autowired
	private JwtConfigure jwtConfigure;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TheaterService theaterService;

	@MockBean
	private UserService userService;

	private static RequestCreateTheater requestCreateTheater;

	@BeforeAll
	static void setUp() {
		requestCreateTheater = new RequestCreateTheater("SEOUL", "theater01");
	}

	@Test
	@DisplayName("인증 성공 테스트")
	void testAuthenticationSuccess() throws Exception {
		//given
		String accessToken = this.jwt.sign(Jwt.Claims.from("pang", "ROLE_ADMIN"));

		//when then
		this.mockMvc.perform(post("/theaters")
				.cookie(new Cookie(this.jwtConfigure.header(), accessToken))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestCreateTheater)))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("인증 실패 - 쿠키 없음")
	void testFailAuthenticationBecauseNoCookie() throws Exception {
		//give when then
		this.mockMvc.perform(post("/theaters")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestCreateTheater)))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.messages.[0]").value(equalTo(UN_AUTHORIZED_EXP_MSG.getMessage())))
			.andExpect(jsonPath("$.statusCode").value(equalTo(HttpStatus.UNAUTHORIZED.value())));
	}

	@Test
	@DisplayName("인가 실패 - 적절한 권한 없음")
	void testFailAuthorityBecauseInvalidRole() throws Exception {
		//given
		String accessToken = this.jwt.sign(Jwt.Claims.from("pang", "ROLE_CUSTOMER"));

		//when then
		this.mockMvc.perform(post("/theaters")
				.cookie(new Cookie(this.jwtConfigure.header(), accessToken))
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(requestCreateTheater)))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.messages.[0]").value(equalTo(ACCESS_DENIED_EXP_MSG.getMessage())))
			.andExpect(jsonPath("$.statusCode").value(equalTo(HttpStatus.FORBIDDEN.value())));
	}
}
