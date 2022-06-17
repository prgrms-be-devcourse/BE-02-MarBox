package prgrms.marco.be02marbox.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "theater_room")
public class TheaterRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id")
	private Theater theater;

	@Column(name = "name")
	private String name;

	@Column(name = "total_seats")
	private Long totalSeats;

	@OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Seat> seats = new ArrayList<>();
}
