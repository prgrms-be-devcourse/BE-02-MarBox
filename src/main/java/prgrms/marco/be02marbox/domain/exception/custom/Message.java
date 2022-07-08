package prgrms.marco.be02marbox.domain.exception.custom;

public enum Message {
	DUPLICATE_EMAIL_EXP_MSG("이미 존재하는 이메일 입니다."),
	DUPLICATE_NAME_EXP_MSG("이미 존재하는 이름입니다."),
	INVALID_EMAIL_EXP_MSG("존재하지 않는 이메일 입니다."),
	ACCESS_DENIED_EXP_MSG("API 접근 권한이 없습니다."),
	UN_AUTHORIZED_EXP_MSG("인증되지 않았습니다."),
	EMPTY_TOKEN_EXP_MSG("토큰은 필수 입니다."),
	EMPTY_USERNAME_EXP_MSG("사용자 이름은 필수 입니다."),
	INVALID_THEATER_EXP_MSG("존재하지 않는 극장 ID 입니다."),
	NOT_EXISTS_USER_TICKET_INFO_EXP_MSG("존재하지 않는 유저의 티켓 정보입니다."),
	NOT_EXISTS_USER_EXP_MSG("존재하지 않는 유저입니다."),
	NOT_EXISTS_TICKET_EXP_MSG("존재하지 않는 티켓입니다."),
	INVALID_DATE_EXP_MSG("해당 날짜에 영화를 상영하지 않습니다."),
	INVALID_SCHEDULE_EXP_MSG("존재하지 않는 스케줄 ID 입니다."),
	INVALID_MOVIE_EXP_MSG("존재하지 않는 영화 ID 입니다."),
	INVALID_THEATER_ROOM_EXP_MSG("존재하지 않는 상영관 ID 입니다."),
	WRONG_PASSWORD_EXP_MSG("비밀번호가 틀렸습니다."),
	ALREADY_UPDATED_TOKEN_EXP_MSG("갱신된 refresh token 입니다."),
	VALID_ACCESS_TOKEN_EXP_MSG("아직 유효한 access token 입니다."),
	INVALID_REFRESH_TOKEN_EXP_MSG("유효하지 않은 refresh token 입니다.");

	private final String message;

	Message(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
