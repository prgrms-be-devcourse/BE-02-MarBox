package prgrms.marco.be02marbox.domain.theater.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;

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
import prgrms.marco.be02marbox.domain.exception.custom.Message;
import prgrms.marco.be02marbox.domain.movie.Genre;
import prgrms.marco.be02marbox.domain.movie.LimitAge;
import prgrms.marco.be02marbox.domain.movie.dto.ResponseFindMovie;
import prgrms.marco.be02marbox.domain.theater.dto.RequestCreateSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSchedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindTime;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;

@WebMvcTest(controllers = ScheduleController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
)
@AutoConfigureRestDocs
class ScheduleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ScheduleService scheduleService;

	@Test
	@DisplayName("????????? ?????? ?????????")
	@WithMockUser(roles = "ADMIN")
	void testCreateSchedule() throws Exception {
		RequestCreateSchedule requestCreateSchedule = new RequestCreateSchedule(1L, 1L, LocalDateTime.now(),
			LocalDateTime.now());

		given(scheduleService.createSchedule(requestCreateSchedule)).willReturn(1L);

		mockMvc.perform(post("/schedules")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestCreateSchedule)))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "schedules/1"))
			.andDo(document("schedule-save",
				requestFields(
					fieldWithPath("theaterRoomId").type(JsonFieldType.NUMBER).description("????????? ID"),
					fieldWithPath("movieId").type(JsonFieldType.NUMBER).description("?????? ID"),
					fieldWithPath("startTime").type(JsonFieldType.STRING).description("?????? ?????? ????????? ??????"),
					fieldWithPath("endTime").type(JsonFieldType.STRING).description("?????? ?????? ????????? ??????")
				)));

	}

	@Test
	@DisplayName("????????? ID??? ???????????? ?????? ?????? ?????? ??????")
	@WithMockUser(roles = "ADMIN")
	void testCreateScheduleFail() throws Exception {
		RequestCreateSchedule requestCreateSchedule = new RequestCreateSchedule(100L, 1L, LocalDateTime.now(),
			LocalDateTime.now());

		given(scheduleService.createSchedule(requestCreateSchedule)).willThrow(
			new IllegalArgumentException("???????????? ?????? ????????? ID"));

		mockMvc.perform(post("/schedules")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestCreateSchedule)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("?????? ???????????? ?????? ????????? ?????? ?????????")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetCurrentMovieList() throws Exception {

		List<ResponseFindMovie> currentMovieList = List.of(
			new ResponseFindMovie("?????????1", LimitAge.CHILD, Genre.ACTION, 100, "test/location"),
			new ResponseFindMovie("?????????2", LimitAge.CHILD, Genre.ROMANCE, 150, "test/location"),
			new ResponseFindMovie("?????????3", LimitAge.ADULT, Genre.ACTION, 120, "test/location"));

		given(scheduleService.findShowingMovieList()).willReturn(currentMovieList);

		mockMvc.perform(get("/schedules/current-movies"))
			.andExpect(status().isOk())
			.andDo(document("schedule-get-current-movies",
				responseFields(
					fieldWithPath("[].name").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("[].limitAge").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("[].genre").type(JsonFieldType.STRING).description("??????"),
					fieldWithPath("[].runningTime").type(JsonFieldType.NUMBER).description("????????????"),
					fieldWithPath("[].posterImgLocation").type(JsonFieldType.STRING).description("????????? ????????? ??????")
				)));
	}

	@Test
	@DisplayName("????????? ID??? ???????????? ?????? ???????????? ?????? ????????? ?????? ?????????")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetMovieListAndDateListInOneTheater() throws Exception {
		List<ResponseFindMovie> movieList = List.of(
			new ResponseFindMovie("??????1", LimitAge.CHILD, Genre.ACTION, 100, "test/location"),
			new ResponseFindMovie("??????2", LimitAge.ADULT, Genre.ROMANCE, 120, "test/location"),
			new ResponseFindMovie("??????3", LimitAge.CHILD, Genre.ANIMATION, 150, "test/location")
		);
		List<LocalDate> dateList = List.of(LocalDate.now(), LocalDate.now().plusDays(1));

		ResponseFindSchedule responseFindMovieListAndDateList =
			new ResponseFindSchedule(movieList, Collections.emptyList(), dateList, Collections.emptyList());

		given(scheduleService.findMovieListAndDateListByTheaterId(1L)).willReturn(responseFindMovieListAndDateList);

		mockMvc.perform(get("/schedules/search")
				.param("theaterId", "1"))
			.andExpect(status().isOk())
			.andDo(document("schedule-get-movie-and-date-in-theater",
				requestParameters(parameterWithName("theaterId").optional().description("????????? ID")),
				responseFields(
					fieldWithPath("movieList[]").type(JsonFieldType.ARRAY).description("?????? ?????????"),
					fieldWithPath("movieList[].name").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("movieList[].limitAge").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
					fieldWithPath("movieList[].genre").type(JsonFieldType.STRING).description("??????"),
					fieldWithPath("movieList[].runningTime").type(JsonFieldType.NUMBER).description("????????????"),
					fieldWithPath("movieList[].posterImgLocation").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
					fieldWithPath("dateList[]").type(JsonFieldType.ARRAY).description("?????? ?????? ?????????"),
					fieldWithPath("theaterList[]").type(JsonFieldType.ARRAY).description("??? ??????"),
					fieldWithPath("timeList[]").type(JsonFieldType.ARRAY).description("??? ??????")
				)));
	}

	@Test
	@DisplayName("???????????? ?????? ????????? ID??? ???????????? 404 Not Found")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetMovieListAndDateListInOneTheater_Fail_Invalid_Theater() throws Exception {
		given(scheduleService.findMovieListAndDateListByTheaterId(200L))
			.willThrow(new EntityNotFoundException(Message.INVALID_THEATER_EXP_MSG.getMessage()));

		mockMvc.perform(get("/schedules/search")
				.param("theaterId", "200"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.messages").exists())
			.andExpect(jsonPath("$.messages[0]").value(Message.INVALID_THEATER_EXP_MSG.getMessage()))
			.andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	@DisplayName("????????? ID??? ????????? ???????????? ?????? ????????? ?????? ?????????")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetMovieListByTheaterIdAndDate() throws Exception {
		List<ResponseFindMovie> movieList = List.of(
			new ResponseFindMovie("??????1", LimitAge.CHILD, Genre.ACTION, 100, "test/location"),
			new ResponseFindMovie("??????2", LimitAge.ADULT, Genre.ROMANCE, 120, "test/location"),
			new ResponseFindMovie("??????3", LimitAge.CHILD, Genre.ANIMATION, 150, "test/location")
		);

		ResponseFindSchedule responseFindMovieList = new ResponseFindSchedule(movieList, Collections.emptyList(),
			Collections.emptyList(), Collections.emptyList());

		given(scheduleService.findMovieListByTheaterIdAndDate(1L, LocalDate.now())).willReturn(responseFindMovieList);

		mockMvc.perform(get("/schedules/search")
				.param("theaterId", "1")
				.param("date", LocalDate.now().toString()))
			.andExpect(status().isOk())
			.andDo(document("schedule-get-movie-by-date-and-theater",
				requestParameters(
					parameterWithName("theaterId").optional().description("????????? ID"),
					parameterWithName("date").optional().description("??????")),
				responseFields(
					fieldWithPath("movieList[]").type(JsonFieldType.ARRAY).description("?????? ?????????"),
					fieldWithPath("movieList[].name").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("movieList[].limitAge").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
					fieldWithPath("movieList[].genre").type(JsonFieldType.STRING).description("??????"),
					fieldWithPath("movieList[].runningTime").type(JsonFieldType.NUMBER).description("????????????"),
					fieldWithPath("movieList[].posterImgLocation").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
					fieldWithPath("dateList[]").type(JsonFieldType.ARRAY).description("??? ??????"),
					fieldWithPath("theaterList[]").type(JsonFieldType.ARRAY).description("??? ??????"),
					fieldWithPath("timeList[]").type(JsonFieldType.ARRAY).description("??? ??????")
				)));
	}

	@Test
	@DisplayName("?????? ID, ????????? ID, ????????? ???????????? ?????? ????????? ?????? ?????????")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetTimeScheduleList() throws Exception {
		List<LocalTime> startTimeListInTheater1 = List.of(LocalTime.of(9, 30), LocalTime.of(14, 25),
			LocalTime.of(16, 45),
			LocalTime.of(19, 30));

		List<LocalTime> startTimeListInTheater2 = List.of(LocalTime.of(20, 40));

		List<ResponseFindTime> timeList = List.of(
			new ResponseFindTime("?????????1", 75, startTimeListInTheater1),
			new ResponseFindTime("?????????2", 100, startTimeListInTheater2)
		);

		ResponseFindSchedule responseFindMovieList = new ResponseFindSchedule(
			Collections.emptyList(),
			Collections.emptyList(),
			Collections.emptyList(),
			timeList);

		given(scheduleService.findTimeScheduleList(1L, 1L, LocalDate.now())).willReturn(responseFindMovieList);

		mockMvc.perform(get("/schedules/search")
				.param("movieId", "1")
				.param("theaterId", "1")
				.param("date", LocalDate.now().toString()))
			.andExpect(status().isOk())
			.andDo(document("schedule-get-timeList-by-movie-and-theater-and-date",
				requestParameters(
					parameterWithName("movieId").optional().description("?????? ID"),
					parameterWithName("theaterId").optional().description("????????? ID"),
					parameterWithName("date").optional().description("??????")),
				responseFields(
					fieldWithPath("timeList[]").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
					fieldWithPath("timeList[].theaterRoomName").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("timeList[].totalSeatCount").type(JsonFieldType.NUMBER).description("????????? ??? ?????? ???"),
					fieldWithPath("timeList[].startTimeList[]").type(JsonFieldType.ARRAY)
						.description("???????????? ?????? ?????? ?????? ??????"),
					fieldWithPath("movieList[]").type(JsonFieldType.ARRAY).description("??? ??????"),
					fieldWithPath("theaterList[]").type(JsonFieldType.ARRAY).description("??? ??????"),
					fieldWithPath("dateList[]").type(JsonFieldType.ARRAY).description("??? ??????")
				)));

	}

}
