package prgrms.marco.be02marbox.domain.theater;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import prgrms.marco.be02marbox.domain.movie.Movie;

@Entity
@Table(name = "schedule")
public class Schedule {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_room_id", nullable = false)
	private TheaterRoom theaterRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;

	protected Schedule() {
	}

	private Schedule(Builder builder) {
		this.id = builder.id;
		this.theaterRoom = builder.theaterRoom;
		this.movie = builder.movie;
		this.startTime = builder.startTime;
		this.endTime = builder.endTime;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Long getId() {
		return id;
	}

	public TheaterRoom getTheaterRoom() {
		return theaterRoom;
	}

	public Movie getMovie() {
		return movie;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public static class Builder {
		private Long id;
		private TheaterRoom theaterRoom;
		private Movie movie;
		private LocalDateTime startTime;
		private LocalDateTime endTime;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder theaterRoom(TheaterRoom theaterRoom) {
			this.theaterRoom = theaterRoom;
			return this;
		}

		public Builder movie(Movie movie) {
			this.movie = movie;
			return this;
		}

		public Builder startTime(LocalDateTime startTime) {
			this.startTime = startTime;
			return this;
		}

		public Builder endTime(LocalDateTime endTime) {
			this.endTime = endTime;
			return this;
		}

		public Schedule build() {
			return new Schedule(this);
		}
	}
}
