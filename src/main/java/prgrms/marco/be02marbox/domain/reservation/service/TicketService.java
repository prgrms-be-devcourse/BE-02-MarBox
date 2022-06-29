package prgrms.marco.be02marbox.domain.reservation.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;
import prgrms.marco.be02marbox.domain.reservation.repository.TicketRepository;
import prgrms.marco.be02marbox.domain.reservation.service.utils.TicketConverter;
import prgrms.marco.be02marbox.domain.theater.repository.ScheduleRepository;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class TicketService {

	private final UserRepository userRepository;
	private final ScheduleRepository scheduleRepository;
	private final TicketRepository ticketRepository;
	private final TicketConverter ticketConverter;

	public TicketService(UserRepository userRepository, ScheduleRepository scheduleRepository,
		TicketRepository ticketRepository, TicketConverter ticketConverter) {
		this.userRepository = userRepository;
		this.scheduleRepository = scheduleRepository;
		this.ticketRepository = ticketRepository;
		this.ticketConverter = ticketConverter;
	}

	public ResponseFindTicket findOneUserTicket(Long userId) {
		Ticket findTicket = ticketRepository.findTicketByUserId(userId)
			.orElseThrow(() -> new EntityNotFoundException(NO_USER_TICKET_INFO_EXP_MSG.getMessage()));
		return ticketConverter.convertFromTicketToResponseFindTicket(findTicket);
	}

	public List<ResponseFindTicket> findTickets() {
		return ticketRepository.findAll()
			.stream()
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}

	public List<ResponseFindTicket> findValidTicketsOfUser(Long userId) {
		return ticketRepository.findAllValidTicketsV2(userId)
			.stream()
			.map(ticketConverter::convertFromTicketToResponseFindTicket)
			.collect(Collectors.toList());
	}
}
