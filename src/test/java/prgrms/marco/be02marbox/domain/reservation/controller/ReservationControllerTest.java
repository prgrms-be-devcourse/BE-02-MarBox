package prgrms.marco.be02marbox.domain.reservation.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.config.WebSecurityConfigure;
import prgrms.marco.be02marbox.domain.reservation.dto.RequestReservation;
import prgrms.marco.be02marbox.domain.reservation.service.ReservationService;


@WebMvcTest(controllers = ReservationController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
)
@AutoConfigureRestDocs
class ReservationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ReservationService reservationService;

	@Test
	@DisplayName("?????? ?????????")
	@WithMockUser(roles = "ADMIN, CUSTOMER")
	void testReservationTicket() throws Exception {
		Long userId = 1L;
		Long scheduleId = 2L;
		List<Long> seatIds = List.of(3L, 4L, 5L);
		RequestReservation request = new RequestReservation(userId, scheduleId, seatIds);

		given(reservationService.reservation(request)).willReturn(1L);

		mockMvc.perform(post("/reservation")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "/tickets/1"))
			.andDo(document("reservation-create",
				requestFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ID"),
					fieldWithPath("scheduleId").type(JsonFieldType.NUMBER).description("????????? ID"),
					fieldWithPath("selectedSeatIds").type(JsonFieldType.ARRAY).description("????????? ??????"))
			));
	}

	@Test
	@DisplayName("?????? ?????? ????????? - ?????? ????????? ??????")
	@WithMockUser(roles = "ADMIN, CUSTOMER")
	void testReservationTicket_Fail_Already_Reserved_Seat() throws Exception {
		Long userId = 1L;
		Long scheduleId = 2L;
		List<Long> seatIds = List.of(3L, 4L, 5L);
		RequestReservation request = new RequestReservation(userId, scheduleId, seatIds);

		given(reservationService.reservation(request))
			.willThrow(new IllegalArgumentException(ALREADY_RESERVED_SEAT_EXP_MSG.getMessage()));

		mockMvc.perform(post("/reservation")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.messages.[0]").value(equalTo(ALREADY_RESERVED_SEAT_EXP_MSG.getMessage())))
			.andExpect(jsonPath("$.statusCode").value(equalTo(HttpStatus.BAD_REQUEST.value())));
	}

	@Test
	@DisplayName("?????? ?????? ????????? - ?????? ??????")
	@WithMockUser(roles = "ADMIN, CUSTOMER")
	void testReservationTicket_Fail_Not_Enough_Money() throws Exception {
		Long userId = 1L;
		Long scheduleId = 2L;
		List<Long> seatIds = List.of(3L, 4L, 5L);
		RequestReservation request = new RequestReservation(userId, scheduleId, seatIds);

		given(reservationService.reservation(request))
			.willThrow(new IllegalArgumentException(NO_MONEY_EXP_MSG.getMessage()));

		mockMvc.perform(post("/reservation")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.messages.[0]").value(equalTo(NO_MONEY_EXP_MSG.getMessage())))
			.andExpect(jsonPath("$.statusCode").value(equalTo(HttpStatus.BAD_REQUEST.value())));
	}
}
