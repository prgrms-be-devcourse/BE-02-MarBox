package prgrms.marco.be02marbox.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.reservation.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
