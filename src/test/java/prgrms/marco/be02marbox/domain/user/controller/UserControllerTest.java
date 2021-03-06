package prgrms.marco.be02marbox.domain.user.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.config.JwtConfigure;
import prgrms.marco.be02marbox.domain.exception.custom.user.DuplicateEmailException;
import prgrms.marco.be02marbox.domain.exception.custom.user.InvalidEmailException;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.dto.RequestSignInUser;
import prgrms.marco.be02marbox.domain.user.dto.RequestSignUpUser;
import prgrms.marco.be02marbox.domain.user.dto.ResponseJwtToken;
import prgrms.marco.be02marbox.domain.user.jwt.Jwt;
import prgrms.marco.be02marbox.domain.user.service.JwtService;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@WebMvcTest(UserController.class)
@EnableConfigurationProperties(JwtConfigure.class)
class UserControllerTest {

	@MockBean
	private UserService userService;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private Jwt jwt;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("회원 가입 실패 - Role 바인딩 실패해 UserSignUpReq role 필드에 null이 들어감")
	void testRoleBindingFail() throws Exception {
		//given
		StringBuilder req = new StringBuilder();
		req.append("{\n")
			.append("\t\"email\" : \"pang@mail.com\",\n")
			.append("\t\"password\" : \"1234\",\n")
			.append("\t\"name\" : \"pang\",\n")
			.append("\t\"Role\" : \"Role\"\n")
			.append("}");

		//when then
		mockMvc.perform(post("/users/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.content(req.toString())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.messages.[0]").value(equalTo("역할은 필수 입니다.")))
			.andExpect(jsonPath("$.statusCode").value(equalTo(HttpStatus.BAD_REQUEST.value())));
	}

	@Test
	@DisplayName("회원 가입 실패 - 존재하는 이메일")
	void testSignUpFailBecauseDuplicateEmail() throws Exception {
		//given
		RequestSignUpUser userSignUpReq = new RequestSignUpUser(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_ADMIN);

		given(userService.create(
			userSignUpReq.email(),
			userSignUpReq.password(),
			userSignUpReq.name(),
			userSignUpReq.role()))
			.willThrow(new DuplicateEmailException(DUPLICATE_EMAIL_EXP_MSG));

		//when then
		mockMvc.perform(post("/users/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userSignUpReq))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.messages.[0]").value(equalTo(DUPLICATE_EMAIL_EXP_MSG.getMessage())))
			.andExpect(jsonPath("$.statusCode").value(equalTo(HttpStatus.BAD_REQUEST.value())));
	}

	@Test
	@DisplayName("회원가입 요청 성공")
	void testSignUpSuccess() throws Exception {
		//given
		RequestSignUpUser userSignUpReq = new RequestSignUpUser(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_ADMIN);

		long userId = 1L;
		given(userService.create(
			userSignUpReq.email(),
			userSignUpReq.password(),
			userSignUpReq.name(),
			userSignUpReq.role())).willReturn(userId);

		//when then
		mockMvc.perform(post("/users/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userSignUpReq)))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "/users/sign-in"));
	}

	@Test
	@DisplayName("로그인 성공")
	void testSignInSuccess() throws Exception {
		//given
		RequestSignInUser requestSignInUser = new RequestSignInUser("pang@email.com", "1234");

		ResponseJwtToken responseJwtToken = new ResponseJwtToken("access-token", "refresh-token");
		given(jwtService.authenticateUser(requestSignInUser.email(), requestSignInUser.password()))
			.willReturn(responseJwtToken);

		//when then
		mockMvc.perform(post("/users/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestSignInUser)))
			.andExpect(status().isNoContent())
			.andExpect(cookie().value("access-token", responseJwtToken.accessToken()))
			.andExpect(cookie().value("refresh-token", responseJwtToken.refreshToken()));
	}

	@Test
	@DisplayName("로그인 실패 - 존재 하지 않는 이메일")
	void testSignInFailBecauseInvalidEmail() throws Exception {
		//given
		RequestSignInUser requestSignInUser = new RequestSignInUser("invalid@email.com", "1234");

		given(jwtService.authenticateUser(requestSignInUser.email(), requestSignInUser.password()))
			.willThrow(new InvalidEmailException(INVALID_EMAIL_EXP_MSG));

		//when then
		mockMvc.perform(post("/users/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestSignInUser)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.messages.[0]").value(equalTo(INVALID_EMAIL_EXP_MSG.getMessage())))
			.andExpect(jsonPath("$.statusCode").value(equalTo(HttpStatus.BAD_REQUEST.value())));
	}

	@Test
	@DisplayName("로그인 실패 - 틀린 비밀번호")
	void testSignInFailBecauseWrongPassword() throws Exception {
		//given
		RequestSignInUser requestSignInUser = new RequestSignInUser(
			"pang@mail.com", "invalid");

		given(jwtService.authenticateUser(requestSignInUser.email(), requestSignInUser.password()))
			.willThrow(new BadCredentialsException("비밀번호가 틀렸습니다."));

		//when then
		mockMvc.perform(post("/users/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestSignInUser)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.messages.[0]").value(equalTo("비밀번호가 틀렸습니다.")))
			.andExpect(jsonPath("$.statusCode").value(equalTo(HttpStatus.BAD_REQUEST.value())));
	}

	@Test
	@DisplayName("토큰 재발급 성공")
	void testRefreshSuccess() throws Exception {
		//given
		String oldAccessToken = "old-access-token";
		String refreshToken = "refresh-token";
		ResponseJwtToken responseJwtToken = new ResponseJwtToken("new-access-token", "new-refresh-token");
		given(jwtService.refreshToken(oldAccessToken, refreshToken))
			.willReturn(responseJwtToken);

		//when then
		mockMvc.perform(post("/users/refresh")
				.cookie(new Cookie("access-token", oldAccessToken),
					new Cookie("refresh-token", refreshToken)))
			.andExpect(status().isNoContent())
			.andExpect(cookie().value("access-token", responseJwtToken.accessToken()))
			.andExpect(cookie().value("refresh-token", responseJwtToken.refreshToken()));
	}
}
