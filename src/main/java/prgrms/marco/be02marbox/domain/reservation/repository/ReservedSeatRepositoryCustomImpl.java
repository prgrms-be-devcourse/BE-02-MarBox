package prgrms.marco.be02marbox.domain.reservation.repository;

import static prgrms.marco.be02marbox.domain.reservation.QReservedSeat.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;

@Repository
public class ReservedSeatRepositoryCustomImpl implements ReservedSeatRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	public ReservedSeatRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public List<ReservedSeat> searchByScheduleIdStartsWith(Long scheduleId) {
		return jpaQueryFactory.selectFrom(reservedSeat)
			.leftJoin(reservedSeat.seat).fetchJoin()
			.where(reservedSeat.id.startsWith(scheduleId + "_" ))
			.fetch();
	}
}
