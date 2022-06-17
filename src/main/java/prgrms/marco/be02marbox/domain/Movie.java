package prgrms.marco.be02marbox.domain;

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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "limit_age")
	private LimitAge limitAge;

	@Column(name = "running_time")
	private Long runningTime;

	@Column(name = "poster_img_location")
	private String posterImgLocation;
}
