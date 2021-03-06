package prgrms.marco.be02marbox.domain.reservation.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.RequestCreateTicket;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;
import prgrms.marco.be02marbox.domain.reservation.repository.ReservedSeatRepository;
import prgrms.marco.be02marbox.domain.reservation.repository.TicketRepository;
import prgrms.marco.be02marbox.domain.reservation.service.utils.TicketConverter;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.theater.repository.SeatRepository;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class TicketService {

	private final UserRepository userRepository;
	private final ScheduleRepository scheduleRepository;
	private final SeatRepository seatRepository;
	private final ReservedSeatRepository reservedSeatRepository;
	private final TicketRepository ticketRepository;
	private final TicketConverter ticketConverter;

	public TicketService(UserRepository userRepository, ScheduleRepository scheduleRepository,
		SeatRepository seatRepository, ReservedSeatRepository reservedSeatRepository, TicketRepository ticketRepository,
		TicketConverter ticketConverter) {
		this.userRepository = userRepository;
		this.scheduleRepository = scheduleRepository;
		this.seatRepository = seatRepository;
		this.reservedSeatRepository = reservedSeatRepository;
		this.ticketRepository = ticketRepository;
		this.ticketConverter = ticketConverter;
	}

	/**
	 *
	 * @param request (userId, scheduleId, ????????????, ????????????)
	 * @return ????????? ????????? Id
	 */
	@Transactional
	public Long createTicket(RequestCreateTicket request) {
		User user = userRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(NOT_EXISTS_USER_EXP_MSG.getMessage()));
		Schedule schedule = scheduleRepository.findById(request.scheduleId())
			.orElseThrow(() -> new EntityNotFoundException(INVALID_MOVIE_EXP_MSG.getMessage()));

		//Account ?????? ????????????, ?????? ?????????, Ticket ?????? else ?????? ?????????

		Ticket newTicket = new Ticket(user, schedule, request.reservedAt());
		Ticket createdTicket = ticketRepository.save(newTicket);
		List<ReservedSeat> selectedSeat = seatRepository.findByTheaterRoomIdAndIdIn(
				schedule.getTheaterRoom().getId(), request.selectedSeatIds())
			.stream()
			.map(seat -> new ReservedSeat(createdTicket, seat))
			.collect(
				Collectors.toList());
		reservedSeatRepository.saveAll(selectedSeat);
		return createdTicket.getId();
	}

	@Transactional
	public Ticket createTicket(User user, Schedule schedule, int payAmount) {
		Ticket newTicket = new Ticket(user, schedule, LocalDateTime.now(), payAmount);
		return ticketRepository.save(newTicket);
	}

	/**
	 *
	 * @param ticketId ??????Id
	 * @return ?????? ?????? ?????? ??????
	 */
	public ResponseFindTicket findTicket(Long ticketId) {
		Ticket findTicket = ticketRepository.findById(ticketId).orElseThrow(
			() -> new EntityNotFoundException(NOT_EXISTS_TICKET_EXP_MSG.getMessage()));
		List<ReservedSeat> findReservedSeats = reservedSeatRepository.searchByScheduleIdStartsWith(
			findTicket.getSchedule().getId());
		return ticketConverter.convertFromTicketToResponseFindTicket(findTicket, findReservedSeats);
	}

	/**
	 * ??????, ???????????? ?????? ????????? ?????? ????????? ???????????? ?????????
	 * @param userId ?????? Id
	 * @return ?????? ?????? ?????? ?????????
	 */

	public List<ResponseFindTicket> findTicketsOfUser(Long userId) {
		return ticketRepository.findAllTicketByUserId(userId)
			.stream()
			.map(ticket -> ticketConverter.convertFromTicketToResponseFindTicket(ticket,
				reservedSeatRepository.searchByScheduleIdStartsWith(ticket.getSchedule().getId())
			))
			.collect(Collectors.toList());
	}

	/**
	 * ???????????? ?????? ?????? ????????? ???????????? ?????????
	 * @return ?????? ????????? ?????? ?????? ?????? ?????????
	 */

	public List<ResponseFindTicket> findTickets() {
		return ticketRepository.findAll()
			.stream()
			.map(ticket -> ticketConverter.convertFromTicketToResponseFindTicket(ticket,
				reservedSeatRepository.searchByScheduleIdStartsWith(ticket.getSchedule().getId())))
			.collect(Collectors.toList());
	}

	/**
	 * ??????, ???????????? ?????? ????????? ?????? ????????? ?????? ????????? ???????????? ?????????
	 * @param userId ?????? ID
	 * @return ?????? ????????? ?????? ?????? ?????? ?????????
	 */

	public List<ResponseFindTicket> findValidTicketsOfUser(Long userId) {
		return ticketRepository.findAllTicketByUserId(userId)
			.stream()
			.filter(ticket -> ticket.getSchedule().getEndTime().isAfter(LocalDateTime.now()))
			.map(ticket -> ticketConverter.convertFromTicketToResponseFindTicket(ticket,
				reservedSeatRepository.searchByScheduleIdStartsWith(ticket.getSchedule().getId())))
			.collect(Collectors.toList());
	}

	/**
	 * ??????, ???????????? ?????? ???????????? ?????? ????????? ???????????? ?????????
	 * @param scheduleId ????????? ID
	 * @return ?????? ???????????? ?????? ?????? ?????? ?????????
	 */
	public List<ResponseFindTicket> findTicketsOfSchedule(Long scheduleId) {
		return ticketRepository.findAllByScheduleId(scheduleId)
			.stream()
			.map(ticket -> ticketConverter.convertFromTicketToResponseFindTicket(ticket,
				reservedSeatRepository.searchByScheduleIdStartsWith(ticket.getSchedule().getId())))
			.collect(Collectors.toList());
	}
}
