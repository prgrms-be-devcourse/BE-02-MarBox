package prgrms.marco.be02marbox.domain.user.dto;

public record ResponseLoginToken(
	String accessToken,
	String refreshToken
) {
}
