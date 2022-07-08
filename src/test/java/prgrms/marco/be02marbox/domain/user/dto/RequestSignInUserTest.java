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

class RequestSignInUserTest {

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
	@DisplayName("검증 실패 - 빈 이메일")
	void testValidationFailBecauseEmptyEmail() {
		//given
		RequestSignInUser requestSignInUser = new RequestSignInUser("", "1234");

		//when
		Set<ConstraintViolation<RequestSignInUser>> violations = validator.validate(requestSignInUser);

		//then
		assertThat(violations).hasSize(1);
		violations.forEach(error -> assertThat(error.getMessage()).isEqualTo("이메일은 필수 입니다."));
	}

	@ParameterizedTest(name = "틀린 이메일 형식 - {0}")
	@ValueSource(strings = {" ", "invalid"})
	void testValidationFailBecauseInvalidEmailFormat(String email) {
		//given
		RequestSignInUser requestSignInUser = new RequestSignInUser(email, "1234");

		//when
		Set<ConstraintViolation<RequestSignInUser>> violations = validator.validate(requestSignInUser);

		//then
		assertThat(violations).hasSize(1);
		violations.forEach(error -> assertThat(error.getMessage()).isEqualTo("이메일 형식이 틀렸습니다."));
	}
}
