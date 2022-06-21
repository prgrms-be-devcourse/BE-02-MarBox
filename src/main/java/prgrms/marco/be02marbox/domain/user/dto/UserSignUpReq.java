package prgrms.marco.be02marbox.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import prgrms.marco.be02marbox.domain.user.Role;

public record UserSignUpReq(
	@NotEmpty(message = "이메일은 필수 입니다.")
	@Email(message = "이메일 형식이 틀렸습니다.")
	String email,
	@Length(min = 4, max = 8, message = "비밀번호는 4글자 이상, 8글자 이하 입니다.")
	String password,
	@NotEmpty(message = "이름은 필수 입니다.")
	String name,
	@NotNull(message = "역할은 필수 입니다.")
	Role role
) {
}
