package prgrms.marco.be02marbox.domain.user.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.user.dto.RequestSignInUser;
import prgrms.marco.be02marbox.domain.user.dto.RequestSignUpUser;
import prgrms.marco.be02marbox.domain.user.dto.ResponseJwtToken;
import prgrms.marco.be02marbox.domain.user.service.JwtService;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private static final String ACCESS_TOKEN = "access-token";
	private static final String REFRESH_TOKEN = "refresh-token";

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

		ResponseJwtToken responseJwtToken = jwtService.authenticateUser(requestSignInUser.email(),
			requestSignInUser.password());

		ResponseCookie accessTokenCookie = generateAccessTokenCookie(responseJwtToken.accessToken());
		ResponseCookie refreshTokenCookie = generateRefreshTokenCookie(responseJwtToken.refreshToken());
		return ResponseEntity
			.noContent()
			.header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString())
			.build();
	}

	@PostMapping("/refresh")
	public ResponseEntity<Void> refreshToken(
		@CookieValue("access-token") String accessToken,
		@CookieValue("refresh-token") String refreshToken) {

		ResponseJwtToken responseJwtToken = jwtService.refreshToken(accessToken, refreshToken);

		ResponseCookie accessTokenCookie = generateAccessTokenCookie(responseJwtToken.accessToken());
		ResponseCookie refreshTokenCookie = generateRefreshTokenCookie(responseJwtToken.refreshToken());
		return ResponseEntity
			.noContent()
			.header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString())
			.build();
	}

	private ResponseCookie generateAccessTokenCookie(String accessToken) {
		return ResponseCookie.from(ACCESS_TOKEN, accessToken)
			.httpOnly(true)
			.path("/")
			.build();
	}

	private ResponseCookie generateRefreshTokenCookie(String refreshToken) {
		return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
			.path("/users/refresh")
			.build();
	}
}
