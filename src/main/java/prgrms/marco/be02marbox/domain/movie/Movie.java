package prgrms.marco.be02marbox.domain.movie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movie")
public class Movie {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "limit_age")
	@Enumerated(value = EnumType.STRING)
	private LimitAge limitAge;

	@Column(name = "genre")
	@Enumerated(value = EnumType.STRING)
	private Genre genre;

	@Column(name = "running_time")
	private Long runningTime;

	@Column(name = "poster_img_location")
	private String posterImgLocation;
}
