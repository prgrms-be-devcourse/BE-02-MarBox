package prgrms.marco.be02marbox.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public record RequestSignInUser(
	@NotEmpty(message = "이메일은 필수 입니다.")
	@Email(message = "이메일 형식이 틀렸습니다.")
	String email,
	@Length(min = 4, max = 8, message = "비밀번호는 4글자 이상, 8글자 이하 입니다.")
	String password
) {
}
