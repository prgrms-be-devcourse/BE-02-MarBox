package prgrms.marco.be02marbox.domain.theater.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;
import static prgrms.marco.be02marbox.domain.theater.dto.document.RequestCreateTheaterRoomDoc.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.config.WebSecurityConfigure;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSeat;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateTheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.document.RequestCreateSeatDoc;
import prgrms.marco.be02marbox.domain.theater.dto.document.RequestCreateTheaterRoomDoc;
import prgrms.marco.be02marbox.domain.theater.service.TheaterCommonService;

@WebMvcTest(controllers = TheaterRoomController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
	}
)
@AutoConfigureRestDocs
class TheaterRoomControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TheaterCommonService theaterCommonService;

	@Autowired
	private ObjectMapper objectMapper;

	private Set<RequestCreateSeat> requestCreateSeats = Set.of(
		new RequestCreateSeat(0, 0),
		new RequestCreateSeat(0, 1)
	);

	private static final String THEATER_ROOM_SAVE_URL = "/theater-rooms";

	@Test
	@DisplayName("관리자는 새로운 상영관을 추가할 수 있다.")
	@WithMockUser(roles = "ADMIN")
	void testSave() throws Exception {
		Long theaterId = 1L;
		RequestCreateTheaterRoom requestDto = new RequestCreateTheaterRoom(theaterId, "A관", requestCreateSeats);
		given(theaterCommonService.saveTheaterRoomWithSeatList(requestDto)).willReturn(theaterId);

		mockMvc.perform(post(THEATER_ROOM_SAVE_URL)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDto))
		)
			.andExpect(status().isCreated())
			.andExpect(redirectedUrl(THEATER_ROOM_SAVE_URL + "/" + theaterId))
			.andDo(document("theater-room-save",
				requestFields()
					.andWithPrefix(SEAT_LIST_PREFIX.getField(), RequestCreateSeatDoc.get())
					.and(RequestCreateTheaterRoomDoc.get())
				)
			);
	}

	@Test
	@DisplayName("극장 ID의 조회 결과가 없는 경우 400 error를 반환한다.")
	@WithMockUser(roles = "ADMIN")
	void testSave_400() throws Exception {
		Long theaterId = 1L;
		RequestCreateTheaterRoom requestDto = new RequestCreateTheaterRoom(theaterId, "A관", requestCreateSeats);

		given(theaterCommonService.saveTheaterRoomWithSeatList(requestDto)).willThrow(
			new IllegalArgumentException(INVALID_THEATER_EXP_MSG.getMessage()));

		mockMvc.perform(post(THEATER_ROOM_SAVE_URL)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDto))
		)
			.andExpect(status().isBadRequest()
			);
	}
}
