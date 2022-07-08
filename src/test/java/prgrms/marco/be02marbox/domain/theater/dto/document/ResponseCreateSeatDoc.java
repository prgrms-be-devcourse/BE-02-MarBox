package prgrms.marco.be02marbox.domain.theater.dto.document;

import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import java.util.List;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public enum ResponseCreateSeatDoc {
	ROW(NUMBER, "row", "행"),
	COL(NUMBER, "col", "열");

	private JsonFieldType type;
	private final String field;
	private final String description;

	ResponseCreateSeatDoc(JsonFieldType type, String field, String description) {
		this.type = type;
		this.field = field;
		this.description = description;
	}

	public JsonFieldType getType() {
		return type;
	}

	public String getField() {
		return field;
	}

	public String getDescription() {
		return description;
	}

	private FieldDescriptor getFieldDescriptor() {
		return fieldWithPath(this.getField()).type(this.getType())
			.description(this.getDescription());
	}

	public static List<FieldDescriptor> get() {
		return List.of(
			ROW.getFieldDescriptor(),
			COL.getFieldDescriptor()
		);
	}
}
