package prgrms.marco.be02marbox.domain.theater.controller;

// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
	@DisplayName("스케줄 생성 테스트")
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
					fieldWithPath("theaterRoomId").type(JsonFieldType.NUMBER).description("상영관 ID"),
					fieldWithPath("movieId").type(JsonFieldType.NUMBER).description("영화 ID"),
					fieldWithPath("startTime").type(JsonFieldType.STRING).description("영화 시작 날짜와 시간"),
					fieldWithPath("endTime").type(JsonFieldType.STRING).description("영화 종료 날짜와 시간")
				)));

	}

	@Test
	@DisplayName("상영관 ID가 존재하지 않는 경우 예외 발생")
	@WithMockUser(roles = "ADMIN")
	void testCreateScheduleFail() throws Exception {
		RequestCreateSchedule requestCreateSchedule = new RequestCreateSchedule(100L, 1L, LocalDateTime.now(),
			LocalDateTime.now());

		given(scheduleService.createSchedule(requestCreateSchedule)).willThrow(
			new IllegalArgumentException("존재하지 않는 상영관 ID"));

		mockMvc.perform(post("/schedules")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestCreateSchedule)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("현재 상영중인 영화 리스트 반환 테스트")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetCurrentMovieList() throws Exception {

		List<ResponseFindMovie> currentMovieList = List.of(
			new ResponseFindMovie("테스트1", LimitAge.CHILD, Genre.ACTION, 100, "test/location"),
			new ResponseFindMovie("테스트2", LimitAge.CHILD, Genre.ROMANCE, 150, "test/location"),
			new ResponseFindMovie("테스트3", LimitAge.ADULT, Genre.ACTION, 120, "test/location"));

		given(scheduleService.findShowingMovieList()).willReturn(currentMovieList);

		mockMvc.perform(get("/schedules/current-movies"))
			.andExpect(status().isOk())
			.andDo(document("schedule-get-current-movies",
				responseFields(
					fieldWithPath("[].name").type(JsonFieldType.STRING).description("영화 이름"),
					fieldWithPath("[].limitAge").type(JsonFieldType.STRING).description("연령 제한"),
					fieldWithPath("[].genre").type(JsonFieldType.STRING).description("장르"),
					fieldWithPath("[].runningTime").type(JsonFieldType.NUMBER).description("상영시간"),
					fieldWithPath("[].posterImgLocation").type(JsonFieldType.STRING).description("포스터 이미지 경로")
				)));
	}

	@Test
	@DisplayName("영화관 ID로 요청하면 영화 리스트와 날짜 리스트 반환 테스트")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetMovieListAndDateListInOneTheater() throws Exception {
		List<ResponseFindMovie> movieList = List.of(
			new ResponseFindMovie("영화1", LimitAge.CHILD, Genre.ACTION, 100, "test/location"),
			new ResponseFindMovie("영화2", LimitAge.ADULT, Genre.ROMANCE, 120, "test/location"),
			new ResponseFindMovie("영화3", LimitAge.CHILD, Genre.ANIMATION, 150, "test/location")
		);
		List<LocalDate> dateList = List.of(LocalDate.now(), LocalDate.now().plusDays(1));

		ResponseFindSchedule responseFindMovieListAndDateList =
			new ResponseFindSchedule(movieList, Collections.emptyList(), dateList, Collections.emptyList());

		given(scheduleService.findMovieListAndDateListByTheaterId(1L)).willReturn(responseFindMovieListAndDateList);

		mockMvc.perform(get("/schedules/search")
				.param("theaterId", "1"))
			.andExpect(status().isOk())
			.andDo(document("schedule-get-movie-and-date-in-theater",
				requestParameters(parameterWithName("theaterId").optional().description("영화관 ID")),
				responseFields(
					fieldWithPath("movieList[]").type(JsonFieldType.ARRAY).description("영화 리스트"),
					fieldWithPath("movieList[].name").type(JsonFieldType.STRING).description("영화 이름"),
					fieldWithPath("movieList[].limitAge").type(JsonFieldType.STRING).description("영화 관람 등급"),
					fieldWithPath("movieList[].genre").type(JsonFieldType.STRING).description("장르"),
					fieldWithPath("movieList[].runningTime").type(JsonFieldType.NUMBER).description("상영시간"),
					fieldWithPath("movieList[].posterImgLocation").type(JsonFieldType.STRING).description("포스터 이미지 경로"),
					fieldWithPath("dateList[]").type(JsonFieldType.ARRAY).description("상영 날짜 리스트"),
					fieldWithPath("theaterList[]").type(JsonFieldType.ARRAY).description("빈 배열"),
					fieldWithPath("timeList[]").type(JsonFieldType.ARRAY).description("빈 배열")
				)));
	}

	@Test
	@DisplayName("존재하지 않는 영화관 ID로 요청하면 404 Not Found")
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
	@DisplayName("영화관 ID와 날짜로 요청하면 영화 리스트 반환 테스트")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetMovieListByTheaterIdAndDate() throws Exception {
		List<ResponseFindMovie> movieList = List.of(
			new ResponseFindMovie("영화1", LimitAge.CHILD, Genre.ACTION, 100, "test/location"),
			new ResponseFindMovie("영화2", LimitAge.ADULT, Genre.ROMANCE, 120, "test/location"),
			new ResponseFindMovie("영화3", LimitAge.CHILD, Genre.ANIMATION, 150, "test/location")
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
					parameterWithName("theaterId").optional().description("영화관 ID"),
					parameterWithName("date").optional().description("날짜")),
				responseFields(
					fieldWithPath("movieList[]").type(JsonFieldType.ARRAY).description("영화 리스트"),
					fieldWithPath("movieList[].name").type(JsonFieldType.STRING).description("영화 이름"),
					fieldWithPath("movieList[].limitAge").type(JsonFieldType.STRING).description("영화 관람 등급"),
					fieldWithPath("movieList[].genre").type(JsonFieldType.STRING).description("장르"),
					fieldWithPath("movieList[].runningTime").type(JsonFieldType.NUMBER).description("상영시간"),
					fieldWithPath("movieList[].posterImgLocation").type(JsonFieldType.STRING).description("포스터 이미지 경로"),
					fieldWithPath("dateList[]").type(JsonFieldType.ARRAY).description("빈 배열"),
					fieldWithPath("theaterList[]").type(JsonFieldType.ARRAY).description("빈 배열"),
					fieldWithPath("timeList[]").type(JsonFieldType.ARRAY).description("빈 배열")
				)));
	}

	@Test
	@DisplayName("영화 ID, 영화관 ID, 날짜로 요청하면 시간 리스트 반환 테스트")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetTimeScheduleList() throws Exception {
		List<LocalTime> startTimeListInTheater1 = List.of(LocalTime.of(9, 30), LocalTime.of(14, 25),
			LocalTime.of(16, 45),
			LocalTime.of(19, 30));

		List<LocalTime> startTimeListInTheater2 = List.of(LocalTime.of(20, 40));

		List<ResponseFindTime> timeList = List.of(
			new ResponseFindTime("상영관1", 75, startTimeListInTheater1),
			new ResponseFindTime("상영관2", 100, startTimeListInTheater2)
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
					parameterWithName("movieId").optional().description("영화 ID"),
					parameterWithName("theaterId").optional().description("영화관 ID"),
					parameterWithName("date").optional().description("날짜")),
				responseFields(
					fieldWithPath("timeList[]").type(JsonFieldType.ARRAY).description("상영 시간 정보"),
					fieldWithPath("timeList[].theaterRoomName").type(JsonFieldType.STRING).description("상영관 이름"),
					fieldWithPath("timeList[].totalSeatCount").type(JsonFieldType.NUMBER).description("상영관 총 좌석 수"),
					fieldWithPath("timeList[].startTimeList[]").type(JsonFieldType.ARRAY)
						.description("상영관의 영화 시작 시간 배열"),
					fieldWithPath("movieList[]").type(JsonFieldType.ARRAY).description("빈 배열"),
					fieldWithPath("theaterList[]").type(JsonFieldType.ARRAY).description("빈 배열"),
					fieldWithPath("dateList[]").type(JsonFieldType.ARRAY).description("빈 배열")
				)));

	}

}
