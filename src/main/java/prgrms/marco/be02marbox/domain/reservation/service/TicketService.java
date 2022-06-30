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

	public List<ResponseFindTicket> findTicketsOfUser(Long userId) {
		return ticketRepository.findAllTicketByUserId(userId)
			.stream()
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}

	public List<ResponseFindTicket> findTickets() {
		return ticketRepository.findAll()
			.stream()
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}

	public List<ResponseFindTicket> findValidTicketsOfUser(Long userId) {
		return ticketRepository.findAllTicketByUserId(userId)
			.stream()
			.filter(ticket -> ticket.getSchedule().getEndTime().isAfter(LocalDateTime.now()))
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}

	public List<ResponseFindTicket> findTicketsOfSchedule(Long scheduleId) {
		return ticketRepository.findAllByScheduleId(scheduleId)
			.stream()
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}
}
