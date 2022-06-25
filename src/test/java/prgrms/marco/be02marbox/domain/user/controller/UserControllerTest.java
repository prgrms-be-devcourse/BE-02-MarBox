package prgrms.marco.be02marbox.domain.user.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static prgrms.marco.be02marbox.domain.user.exception.Message.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.config.JwtConfigure;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.dto.RequestSignInUser;
import prgrms.marco.be02marbox.domain.user.dto.ResponseLoginUser;
import prgrms.marco.be02marbox.domain.user.dto.UserSignUpReq;
import prgrms.marco.be02marbox.domain.user.exception.DuplicateEmailException;
import prgrms.marco.be02marbox.domain.user.exception.DuplicateNameException;
import prgrms.marco.be02marbox.domain.user.exception.InvalidEmailException;
import prgrms.marco.be02marbox.domain.user.jwt.Jwt;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@WebMvcTest(UserController.class)
@EnableConfigurationProperties(JwtConfigure.class)
class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Jwt jwt;

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
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.message.[0]").value(equalTo("역할은 필수 입니다.")));
	}

	@Test
	@DisplayName("회원 가입 실패 - 존재하는 이메일")
	void testSignUpFailBecauseDuplicateEmail() throws Exception {
		//given
		UserSignUpReq userSignUpReq = new UserSignUpReq(
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
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.message").value(DUPLICATE_EMAIL_EXP_MSG.getMessage()));
	}

	@Test
	@DisplayName("회원 가입 실패 - 존재하는 이름")
	void testSignUpFailBecauseDuplicateName() throws Exception {
		//given
		UserSignUpReq userSignUpReq = new UserSignUpReq(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_ADMIN);

		given(userService.create(
			userSignUpReq.email(),
			userSignUpReq.password(),
			userSignUpReq.name(),
			userSignUpReq.role()))
			.willThrow(new DuplicateNameException(DUPLICATE_NAME_EXP_MSG));

		//when then
		mockMvc.perform(post("/users/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userSignUpReq))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.message").value(DUPLICATE_NAME_EXP_MSG.getMessage()));
	}

	@Test
	@DisplayName("회원가입 요청 성공")
	void testSignUpSuccess() throws Exception {
		//given
		UserSignUpReq userSignUpReq = new UserSignUpReq(
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
			.andExpect(header().string("location", "/users/sign-in"))
			.andExpect(jsonPath("$.id").value(userId));
	}

	@Test
	@DisplayName("로그인 성공")
	void testSignInSuccess() throws Exception {
		//given
		String email = "pang@email.com";
		String password = "1234";
		RequestSignInUser requestSignInUser = new RequestSignInUser(email, password);

		String name = "pang";
		String role = "ROLE_ADMIN";
		ResponseLoginUser responseLoginUser = new ResponseLoginUser(name, role);
		given(userService.login(email, password)).willReturn(responseLoginUser);

		String token = jwt.sign(Jwt.Claims.from(name, role));

		//when then
		mockMvc.perform(post("/users/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestSignInUser)))
			.andExpect(status().isNoContent())
			.andExpect(header().string("token", token));
	}

	@Test
	@DisplayName("로그인 실패 - 존재 하지 않는 이메일")
	void testSignInFailBecauseInvalidEmail() throws Exception {
		//given
		String email = "pang@email.com";
		String password = "1234";
		RequestSignInUser requestSignInUser = new RequestSignInUser(email, password);

		given(userService.login(email, password))
			.willThrow(new InvalidEmailException(INVALID_EMAIL_EXP_MSG));

		//when then
		mockMvc.perform(post("/users/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestSignInUser)))
			.andExpect(status().is4xxClientError())
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 실패 - 틀린 비밀번호")
	void testSignInFailBecauseWrongPassword() throws Exception {
		//given
		String email = "pang@email.com";
		String password = "1234";
		RequestSignInUser requestSignInUser = new RequestSignInUser(email, password);

		given(userService.login(email, password))
			.willThrow(new BadCredentialsException("비밀번호가 틀렸습니다."));

		//when then
		mockMvc.perform(post("/users/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestSignInUser)))
			.andExpect(status().is4xxClientError())
			.andDo(print());
	}
}
