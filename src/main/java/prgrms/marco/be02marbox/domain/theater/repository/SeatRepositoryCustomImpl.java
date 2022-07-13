package prgrms.marco.be02marbox.domain.theater.repository;

import static prgrms.marco.be02marbox.domain.theater.QSeat.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import prgrms.marco.be02marbox.domain.theater.Seat;

@Repository
public class SeatRepositoryCustomImpl implements SeatRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	public SeatRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public List<Seat> findByTheaterRoomIdAndIdIn(Long theaterRoomId, List<Long> reservedSeatIdList) {
		return jpaQueryFactory.selectFrom(seat)
			.where(seat.theaterRoom.id.eq(theaterRoomId).and(seat.id.in(reservedSeatIdList)))
			.fetch();
	}

	@Override
	public List<Seat> findByTheaterRoomIdAndIdNotIn(Long theaterRoomId, List<Long> reservedSeatIdList) {
		return jpaQueryFactory.selectFrom(seat)
			.where(seat.theaterRoom.id.eq(theaterRoomId).and(seat.id.notIn(reservedSeatIdList)))
			.fetch();
	}

	@Override
	public List<Seat> findByIdIn(List<Long> seatIdList) {
		return jpaQueryFactory.selectFrom(seat)
			.where(seat.id.in(seatIdList))
			.fetch();
	}
}
