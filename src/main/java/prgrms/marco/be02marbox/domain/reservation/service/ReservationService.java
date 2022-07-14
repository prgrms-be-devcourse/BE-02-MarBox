package prgrms.marco.be02marbox.domain.reservation.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.RequestReservation;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindReservedSeat;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;
import prgrms.marco.be02marbox.domain.theater.service.SeatService;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@Service
@Transactional(readOnly = true)
public class ReservationService {
	private static final int TICKET_PRICE = 10000;

	private final ReservedSeatService reservedSeatService;
	private final SeatService seatService;
	private final ScheduleService scheduleService;
	private final TicketService ticketService;
	private final PayService payService;
	private final UserService userService;

	public ReservationService(ReservedSeatService reservedSeatService,
		SeatService seatService, ScheduleService scheduleService,
		TicketService ticketService, PayService payService,
		UserService userService) {
		this.reservedSeatService = reservedSeatService;
		this.seatService = seatService;
		this.scheduleService = scheduleService;
		this.ticketService = ticketService;
		this.payService = payService;
		this.userService = userService;
	}

	/**
	 * 스케줄 별 예약좌석 조회
	 *
	 * @param scheduleId 스케줄 id
	 * @return 예약 좌석리스트
	 */
	public List<ResponseFindReservedSeat> findReservePossibleSeatList(Long scheduleId) {
		Schedule schedule = scheduleService.findById(scheduleId);

		Set<Long> reservedSeatIdList = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		return seatService.findAvailableSeatList(schedule.getTheaterRoom().getId(), reservedSeatIdList);
	}

	/**
	 *
	 * @param requestReservation
	 * @return 티켓 Id
	 */
	@Transactional
	public Long reservation(RequestReservation requestReservation) {
		User findUser = userService.findById(requestReservation.userId());
		// 결제
		List<Long> seats = requestReservation.selectedSeatIds();
		int payAmount = TICKET_PRICE * seats.size();
		payService.pay(findUser, payAmount);

		//티켓 저장
		Schedule findSchedule = scheduleService.findById(requestReservation.scheduleId());
		Ticket savedTicket = ticketService.createTicket(findUser, findSchedule, payAmount);

		// 좌석 예약 처리
		List<Seat> findSeats = seatService.findSeatsByIdIn(seats);
		List<ReservedSeat> selectedSeats = findSeats.stream()
			.map(seat -> new ReservedSeat(savedTicket, seat))
			.collect(Collectors.toList());

		try {
			reservedSeatService.saveAllAndFlush(selectedSeats);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException(ALREADY_RESERVED_SEAT_EXP_MSG.getMessage());
		}

		return savedTicket.getId();
	}
}
