package prgrms.marco.be02marbox.domain.reservation.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.RequestCreateTicket;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;
import prgrms.marco.be02marbox.domain.reservation.repository.TicketRepository;
import prgrms.marco.be02marbox.domain.reservation.service.utils.TicketConverter;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class TicketService {

	private final UserRepository userRepository;
	private final ScheduleRepository scheduleRepository;
	private final TicketRepository ticketRepository;
	private final TicketConverter ticketConverter;

	public TicketService(UserRepository userRepository, ScheduleRepository scheduleRepository,
		TicketRepository ticketRepository,
		TicketConverter ticketConverter) {
		this.userRepository = userRepository;
		this.scheduleRepository = scheduleRepository;
		this.ticketRepository = ticketRepository;
		this.ticketConverter = ticketConverter;
	}

	@Transactional
	public Long createTicket(RequestCreateTicket request) {
		User user = userRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(NOT_EXISTS_USER_EXP_MSG.getMessage()));
		Schedule schedule = scheduleRepository.findById(request.scheduleId())
			.orElseThrow(() -> new EntityNotFoundException(INVALID_MOVIE_EXP_MSG.getMessage()));
		return ticketRepository.save(new Ticket(user, schedule, request.reservedAt())).getId();
	}

	public ResponseFindTicket findTicket(Long ticketId) {
		Ticket findTicket = ticketRepository.findById(ticketId).orElseThrow(
			() -> new EntityNotFoundException(NOT_EXISTS_TICKET_EXP_MSG.getMessage()));
		return ticketConverter.convertFromTicketToResponseFindTicket(findTicket);
	}

	/**
	 * 고객, 관리자가 특정 고객의 예매 내역을 조회하는 메서드
	 * @param userId 유저 Id
	 * @return 티켓 정보 조회 리스트
	 */

	public List<ResponseFindTicket> findTicketsOfUser(Long userId) {
		return ticketRepository.findAllTicketByUserId(userId)
			.stream()
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}

	/**
	 * 관리자가 전체 예매 내역을 조회하는 메서드
	 * @return 전체 고객의 전체 티켓 정보 리스트
	 */

	public List<ResponseFindTicket> findTickets() {
		return ticketRepository.findAll()
			.stream()
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}

	/**
	 * 고객, 관리자가 특정 고객의 사용 가능한 예매 내역을 조회하는 메서드
	 * @param userId 유저 ID
	 * @return 특정 고객의 전체 예매 내역 리스트
	 */

	public List<ResponseFindTicket> findValidTicketsOfUser(Long userId) {
		return ticketRepository.findAllTicketByUserId(userId)
			.stream()
			.filter(ticket -> ticket.getSchedule().getEndTime().isAfter(LocalDateTime.now()))
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}

	/**
	 * 관리자가 특정 스케줄의 예매 내역을 조회하는 메서드
	 * @param scheduleId 스케줄 ID
	 * @return 특정 스케줄의 전체 예매 내역 리스트
	 */
	public List<ResponseFindTicket> findTicketsOfSchedule(Long scheduleId) {
		return ticketRepository.findAllByScheduleId(scheduleId)
			.stream()
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}
}
