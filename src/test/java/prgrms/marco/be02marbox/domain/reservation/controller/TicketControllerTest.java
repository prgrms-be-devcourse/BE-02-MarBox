package prgrms.marco.be02marbox.domain.reservation.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import prgrms.marco.be02marbox.config.WebSecurityConfigure;
import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.Movie;
import prgrms.marco.be02marbox.domain.reservation.dto.RequestCreateTicket;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;
import prgrms.marco.be02marbox.domain.reservation.service.TicketService;
import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Theater;
import prgrms.marco.be02marbox.domain.theater.TheaterRoom;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;

@WebMvcTest(controllers = TicketController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class))
@AutoConfigureRestDocs
class TicketControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@MockBean
	TicketService ticketService;

	private User user1;
	private User user2;
	private Schedule schedule1;
	private Schedule schedule2;
	private Theater theater;
	private TheaterRoom theaterRoom;
	private Movie movie;
	private ResponseFindTicket response1;
	private ResponseFindTicket response2;
	private List<ResponseFindTicket> responses;

	@BeforeEach
	void initData() {
		LocalDateTime inValidTime = LocalDateTime.of(2022, 6, 30, 13, 0);
		LocalDateTime validTime = LocalDateTime.of(2100, 6, 25, 13, 0);
		user1 = new User("test@naver.com", "password1234", "wisehero1", Role.ROLE_CUSTOMER);
		user2 = new User("test@gmail.com", "password1234", "wisehero2", Role.ROLE_CUSTOMER);
		movie = new Movie("movie0", LimitAge.ADULT, Genre.ACTION, 120);
		theater = new Theater(Region.BUSAN, "theater0");
		theaterRoom = new TheaterRoom(theater, "first");
		schedule1 = Schedule.builder()
			.movie(movie)
			.theaterRoom(theaterRoom)
			.startTime(inValidTime)
			.endTime(inValidTime.plusHours(2))
			.build();
		schedule2 = Schedule.builder()
			.movie(movie)
			.theaterRoom(theaterRoom)
			.startTime(validTime.minusHours(2))
			.endTime(validTime)
			.build();

		response1 = new ResponseFindTicket(user1.getName(), movie.getName(),
			movie.getLimitAge(), theater.getName(),
			theaterRoom.getName(), schedule1.getStartTime(), schedule1.getEndTime(),
			List.of(new ResponseFindSeat(0, 0), new ResponseFindSeat(0, 1)));
		response2 = new ResponseFindTicket(user2.getName(), movie.getName(),
			movie.getLimitAge(), theater.getName(),
			theaterRoom.getName(), schedule2.getStartTime(), schedule2.getEndTime(),
			List.of(new ResponseFindSeat(0, 0), new ResponseFindSeat(0, 1)));
		responses = new ArrayList<>();
		responses.add(response1);
		responses.add(response2);
	}

	@Test
	@DisplayName("?????? ?????? ?????????")
	@WithMockUser(roles = "ADMIN, CUSTOMER")
	void testSaveTicket() throws Exception {
		// given
		List<Long> selectedSeats = List.of(1L, 2L);
		RequestCreateTicket request = new RequestCreateTicket(1L, 1L,
			LocalDateTime.now().minusHours(1), selectedSeats);
		ResponseFindTicket responseFindTicket = new ResponseFindTicket(user1.getName(), movie.getName(),
			movie.getLimitAge(), theater.getName(),
			theaterRoom.getName(), schedule1.getStartTime(), schedule1.getEndTime(),
			List.of(new ResponseFindSeat(0, 0), new ResponseFindSeat(0, 1)));
		given(ticketService.createTicket(request)).willReturn(1L);
		given(ticketService.findTicket(1L))
			.willReturn(responseFindTicket);

		// expected
		mockMvc.perform(post("/tickets")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andDo(document("ticket-save",
				requestFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ID"),
					fieldWithPath("scheduleId").type(JsonFieldType.NUMBER).description("????????? ID"),
					fieldWithPath("reservedAt").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("selectedSeatIds").type(JsonFieldType.ARRAY).description("?????? ??????")),
				responseFields(
					fieldWithPath("username").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("movieName").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("limitAge").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("theaterName").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("theaterRoomName").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("startTime").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
					fieldWithPath("endTime").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
					fieldWithPath("seat[].row").type(JsonFieldType.NUMBER).description("?????? ??? ??????"),
					fieldWithPath("seat[].col").type(JsonFieldType.NUMBER).description("?????? ??? ??????")
				)));
	}

	@Test
	@DisplayName("?????? ?????? ?????? ?????????")
	@WithMockUser(roles = "ADMIN, CUSTOMER")
	void testGetTicket() throws Exception {
		// given
		Long ticketId = 1L;
		ResponseFindTicket responseFindTicket = new ResponseFindTicket(user1.getName(), movie.getName(),
			movie.getLimitAge(), theater.getName(),
			theaterRoom.getName(), schedule1.getStartTime(), schedule1.getEndTime(),
			List.of(new ResponseFindSeat(0, 0), new ResponseFindSeat(0, 1)));
		given(ticketService.findTicket(ticketId)).willReturn(responseFindTicket);

		// expected
		mockMvc.perform(get("/tickets/{ticketId}", ticketId)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("wisehero1"))
			.andExpect(jsonPath("$.movieName").value("movie0"))
			.andExpect(jsonPath("$.limitAge").value("ADULT"))
			.andExpect(jsonPath("$.theaterName").value("theater0"))
			.andExpect(jsonPath("$.theaterRoomName").value("first"))
			.andExpect(jsonPath("$.seat[0].row").value(0))
			.andExpect(jsonPath("$.seat[0].col").value(0))
			.andDo(document("ticket-find-by-ticketId",
				pathParameters(
					parameterWithName("ticketId").description("?????? ID")),
				responseFields(
					fieldWithPath("username").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("movieName").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("limitAge").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("theaterName").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("theaterRoomName").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("startTime").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
					fieldWithPath("endTime").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
					fieldWithPath("seat[].row").type(JsonFieldType.NUMBER).description("?????? ??? ??????"),
					fieldWithPath("seat[].col").type(JsonFieldType.NUMBER).description("?????? ??? ??????")
				)));
	}

	@Test
	@DisplayName("?????? ?????? ?????? ?????????")
	@WithMockUser(roles = "ADMIN, CUSTOMER")
	void testGetAllTicket() throws Exception {
		// given
		given(ticketService.findTickets()).willReturn(responses);

		//expected
		mockMvc.perform(get("/tickets")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("ticket-find-all"));
	}

	@Test
	@DisplayName("?????? ?????? ?????? ?????? ?????????")
	@WithMockUser(roles = "ADMIN, CUSTOMER")
	void testGetTicketsOfUser() throws Exception {
		// given
		Long userId = 1L;

		given(ticketService.findTicketsOfUser(userId)).willReturn(List.of(response1));

		mockMvc.perform(get("/tickets")
				.param("user-id", "1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("ticket-find-by-userId",
				requestParameters(parameterWithName("user-id").description("?????? ID"))));

	}

	@Test
	@DisplayName("?????? ???????????? ?????? ?????? ?????? ?????????")
	@WithMockUser(roles = "ADMIN, CUSTOMER")
	void testGetTicketsOfSchedule() throws Exception {
		Long scheduleId = 1L;
		given(ticketService.findTicketsOfSchedule(scheduleId)).willReturn(List.of(response1));

		mockMvc.perform(get("/tickets")
				.param("schedule_id", "1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("ticket-find-by-scheduleId",
				requestParameters(parameterWithName("schedule_id").description("????????? ID"))));
	}

	@Test
	@DisplayName("?????? ????????? ????????? ?????? ?????? ?????? ?????????")
	@WithMockUser(roles = "ADMIN, CUSTOMER")
	void testGetValidTicketsOfUser() throws Exception {

		Long userId = 2L;
		given(ticketService.findValidTicketsOfUser(userId)).willReturn(List.of(response2));

		mockMvc.perform(get("/tickets/valid")
				.param("user-id", "2")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("ticket-find-valid-ticket",
				requestParameters(parameterWithName("user-id").description("?????? ID"))));
	}
}
