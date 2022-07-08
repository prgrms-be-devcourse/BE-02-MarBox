package prgrms.marco.be02marbox.domain.user.dto;

import prgrms.marco.be02marbox.domain.user.Role;

public record ResponseLoginUser(
	String name,
	Role role
) {
}
