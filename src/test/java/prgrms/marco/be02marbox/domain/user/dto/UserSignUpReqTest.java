package prgrms.marco.be02marbox.domain.user.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import prgrms.marco.be02marbox.domain.user.Role;

class UserSignUpReqTest {

	private static ValidatorFactory factory;
	private static Validator validator;

	@BeforeAll
	static void setUp() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@AfterAll
	static void close() {
		factory.close();
	}

	@Test
	@DisplayName("빈 이메일 전송시 예외 발생")
	void testEmptyEmail() {
		//given
		UserSignUpReq userSignUpReq = new UserSignUpReq(
			"",
			"1234",
			"pang",
			Role.ROLE_CUSTOMER);

		//when
		Set<ConstraintViolation<UserSignUpReq>> violations = validator.validate(userSignUpReq);

		//then
		assertThat(violations).isNotEmpty();
		violations.forEach(error -> assertThat(error.getMessage()).isEqualTo("이메일은 필수 입니다."));
	}

	@Test
	@DisplayName("유효하지 않은 이메일 형식")
	void testInvalidFormatEmail() {
		//given
		UserSignUpReq userSignUpReq = new UserSignUpReq(
			"aaaaa",
			"1234",
			"pang",
			Role.ROLE_CUSTOMER);

		//when
		Set<ConstraintViolation<UserSignUpReq>> violations = validator.validate(userSignUpReq);

		//then
		assertThat(violations).isNotEmpty();
		violations.forEach(error -> assertThat(error.getMessage()).isEqualTo("이메일 형식이 틀렸습니다."));
	}

	@ParameterizedTest(name = "틀린 비밀번호 형식 - {0}")
	@ValueSource(strings = {"", "123", "123456789"})
	void testInvalidFormatPassword(String password) {
		//given
		UserSignUpReq userSignUpReq = new UserSignUpReq(
			"pang@email.com",
			password,
			"pang",
			Role.ROLE_CUSTOMER);

		//when
		Set<ConstraintViolation<UserSignUpReq>> violations = validator.validate(userSignUpReq);

		//then
		assertThat(violations).isNotEmpty();
		violations.forEach(error -> assertThat(error.getMessage()).isEqualTo("비밀번호는 4글자 이상, 8글자 이하 입니다."));
	}

	@Test
	@DisplayName("빈 이메일 전송시 예외 발생")
	void testEmptyName() {
		//given
		UserSignUpReq userSignUpReq = new UserSignUpReq(
			"pang@email.com",
			"1234",
			"",
			Role.ROLE_CUSTOMER);

		//when
		Set<ConstraintViolation<UserSignUpReq>> violations = validator.validate(userSignUpReq);

		//then
		assertThat(violations).isNotEmpty();
		violations.forEach(error -> assertThat(error.getMessage()).isEqualTo("이름은 필수 입니다."));
	}
}
