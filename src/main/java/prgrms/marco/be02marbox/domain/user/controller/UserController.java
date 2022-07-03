package prgrms.marco.be02marbox.domain.user.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.user.dto.RequestSignInUser;
import prgrms.marco.be02marbox.domain.user.dto.RequestSignUpUser;
import prgrms.marco.be02marbox.domain.user.dto.ResponseLoginToken;
import prgrms.marco.be02marbox.domain.user.service.JwtService;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	public static final String ACCESS_TOKEN = "access-token";
	public static final String REFRESH_TOKEN = "refresh-token";

	private final UserService userService;
	private final JwtService jwtService;

	public UserController(UserService userService, JwtService jwtService) {
		this.userService = userService;
		this.jwtService = jwtService;
	}

	@PostMapping("/sign-up")
	public ResponseEntity<Void> signUp(
		@Validated @RequestBody RequestSignUpUser userSignUpReq) {

		userService.create(
			userSignUpReq.email(),
			userSignUpReq.password(),
			userSignUpReq.name(),
			userSignUpReq.role());

		return ResponseEntity
			.created(URI.create("/users/sign-in"))
			.build();
	}

	@PostMapping("/sign-in")
	public ResponseEntity<Void> signIn(
		@Validated @RequestBody RequestSignInUser requestSignInUser) {

		ResponseLoginToken responseLoginToken = jwtService.authenticateUser(requestSignInUser.email(),
			requestSignInUser.password());

		ResponseCookie accessToken = ResponseCookie.from(ACCESS_TOKEN, responseLoginToken.accessToken())
			.httpOnly(true)
			.path("/")
			.build();

		ResponseCookie refreshToken = ResponseCookie.from(REFRESH_TOKEN, responseLoginToken.refreshToken())
			.path("/users/refresh")
			.build();

		return ResponseEntity
			.noContent()
			.header(HttpHeaders.SET_COOKIE, accessToken.toString(), refreshToken.toString())
			.build();
	}
}
