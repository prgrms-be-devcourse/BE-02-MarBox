package prgrms.marco.be02marbox.domain.user.dto;

public record ResponseJwtToken(
	String accessToken,
	String refreshToken
) {
}
