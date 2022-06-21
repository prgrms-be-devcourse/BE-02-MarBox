package prgrms.marco.be02marbox.domain.user.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.dto.UserSignUpReq;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Role binding 실패하면 UserSignUpReq.Role에 Null이 들어감")
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
}
