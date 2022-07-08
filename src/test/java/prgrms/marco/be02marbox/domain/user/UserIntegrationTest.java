package prgrms.marco.be02marbox.domain.user;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.domain.user.dto.RequestSignInUser;
import prgrms.marco.be02marbox.domain.user.dto.RequestSignUpUser;
import prgrms.marco.be02marbox.domain.user.jwt.Jwt;
import prgrms.marco.be02marbox.domain.user.repository.RefreshTokenRedisRepository;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("integration")
class UserIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Autowired
	private Jwt jwt;

	@BeforeEach
	void clean() {
		this.refreshTokenRedisRepository.deleteAll();
	}

	@Test
	@DisplayName("사용자 회원 가입 API 성공")
	void testSingUpApiSuccess() throws Exception {
		//given
		RequestSignUpUser requestSignUpUser = new RequestSignUpUser(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_ADMIN);

		//when then
		mockMvc.perform(post("/users/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestSignUpUser))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(document("user-sign-up",
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입")
				),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
					fieldWithPath("role").type(JsonFieldType.STRING).description("사용자 역할")
				),
				responseHeaders(
					headerWithName(HttpHeaders.LOCATION).description("로그인 API 위치")
				)));
	}

	@Test
	@DisplayName("사용자 로그인 API 성공")
	void testSignInApiSuccess() throws Exception {
		//given
		RequestSignUpUser requestSignUpUser = new RequestSignUpUser(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_ADMIN);

		Long savedUserId = userService.create(
			requestSignUpUser.email(),
			requestSignUpUser.password(),
			requestSignUpUser.name(),
			requestSignUpUser.role());

		RequestSignInUser requestSignInUser =
			new RequestSignInUser(requestSignUpUser.email(), requestSignUpUser.password());

		//when then
		mockMvc.perform(post("/users/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestSignInUser)))
			.andExpect(status().isNoContent())
			.andDo(document("user-sign-in",
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입")
				),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호")
				),
				responseHeaders(
					headerWithName(HttpHeaders.SET_COOKIE).description("쿠키 세팅")
				)));
	}

	@Test
	@DisplayName("토큰 재발급 API 성공")
	void testRefreshApiSuccess() throws Exception {
		//given
		User savedUser = userRepository.save(new User(
			"pang@mail.com",
			"encrypted",
			"pang",
			Role.ROLE_ADMIN));

		String accessToken = jwt.generateAccessToken(savedUser.getEmail(), savedUser.getRole());
		String refreshToken = jwt.generateRefreshToken(savedUser.getEmail());
		refreshTokenRedisRepository.save(new RefreshToken(savedUser.getEmail(), refreshToken));

		//access token 유효기간 만료시키기
		Thread.sleep(1000);

		//when then
		mockMvc.perform(post("/users/refresh")
				.cookie(new Cookie("access-token", accessToken),
					new Cookie("refresh-token", refreshToken)))
			.andExpect(status().isNoContent())
			.andDo(document("user-refresh",
				responseHeaders(
					headerWithName(HttpHeaders.SET_COOKIE).description("new access token, new refresh token")
				)));
	}
}
