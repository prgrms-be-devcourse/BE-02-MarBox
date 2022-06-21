package prgrms.marco.be02marbox.domain.user.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.user.dto.UserSignUpReq;
import prgrms.marco.be02marbox.domain.user.dto.UserSignUpRes;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/sign-up")
	public ResponseEntity<UserSignUpRes> signUp(
		@Validated @RequestBody UserSignUpReq userSignUpReq) {

		Long userId = userService.create(
			userSignUpReq.email(),
			userSignUpReq.password(),
			userSignUpReq.name(),
			userSignUpReq.role());

		return ResponseEntity
			.created(URI.create("/users/sign-in"))
			.body(new UserSignUpRes(userId));
	}
}
