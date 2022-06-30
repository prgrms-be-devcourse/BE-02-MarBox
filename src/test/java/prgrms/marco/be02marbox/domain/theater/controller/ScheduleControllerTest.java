package prgrms.marco.be02marbox.domain.theater.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
			new ResponseFindSchedule(movieList, null, dateList, null);

		given(scheduleService.findMovieListAndDateListByTheaterId(1L)).willReturn(responseFindMovieListAndDateList);

		mockMvc.perform(get("/schedules")
				.param("theaterId", "1"))
			.andExpect(status().isOk())
			.andDo(document("schedule-get-movie-and-date-in-theater",
				responseFields(
					fieldWithPath("movieList[]").type(JsonFieldType.ARRAY).description("영화 리스트"),
					fieldWithPath("movieList[].name").type(JsonFieldType.STRING).description("영화 이름"),
					fieldWithPath("movieList[].limitAge").type(JsonFieldType.STRING).description("영화 관람 등급"),
					fieldWithPath("movieList[].genre").type(JsonFieldType.STRING).description("장르"),
					fieldWithPath("movieList[].runningTime").type(JsonFieldType.NUMBER).description("상영시간"),
					fieldWithPath("movieList[].posterImgLocation").type(JsonFieldType.STRING).description("포스터 이미지 경로"),
					fieldWithPath("dateList[]").type(JsonFieldType.ARRAY).description("상영 날짜 리스트"),
					fieldWithPath("theaterList").type(JsonFieldType.NULL).description("영화관 리스트"),
					fieldWithPath("timeList").type(JsonFieldType.NULL).description("상영 시간 리스트")
				)));
	}

	@Test
	@DisplayName("존재하지 않는 영화관 ID로 요청하면 404 Not Found")
	@WithMockUser(roles = {"ADMIN", "USER"})
	void testGetMovieListAndDateListInOneTheater_Fail_Invalid_Theater() throws Exception {
		given(scheduleService.findMovieListAndDateListByTheaterId(200L))
			.willThrow(new EntityNotFoundException(Message.INVALID_THEATER_EXP_MSG.getMessage()));

		mockMvc.perform(get("/schedules")
				.param("theaterId", "200"))
			.andExpect(status().isNotFound());
	}

}
