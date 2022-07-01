package prgrms.marco.be02marbox.domain.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;
import prgrms.marco.be02marbox.domain.reservation.repository.TicketRepository;
import prgrms.marco.be02marbox.domain.reservation.service.utils.TicketConverter;

@Service
@Transactional(readOnly = true)
public class TicketService {

	private final TicketRepository ticketRepository;
	private final TicketConverter ticketConverter;

	public TicketService(TicketRepository ticketRepository, TicketConverter ticketConverter) {
		this.ticketRepository = ticketRepository;
		this.ticketConverter = ticketConverter;
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
