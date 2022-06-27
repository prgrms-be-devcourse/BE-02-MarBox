package prgrms.marco.be02marbox.domain.exception.dto;

import java.util.List;

public record ResponseApiError(
	List<String> messages,
	int statusCode
) {
}
