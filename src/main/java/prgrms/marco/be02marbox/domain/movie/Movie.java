package prgrms.marco.be02marbox.domain.movie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "movie")
public class Movie {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Column(name = "name")
	private String name;

	@Column(name = "limit_age")
	@NotNull
	@Enumerated(value = EnumType.STRING)
	private LimitAge limitAge;

	@Column(name = "genre")
	@NotNull
	@Enumerated(value = EnumType.STRING)
	private Genre genre;

	@Column(name = "running_time")
	@NotNull
	@Min(1)
	private Integer runningTime;

	@Column(name = "poster_img_location")
	private String posterImgLocation;

	protected Movie() {
	}

	public Movie(String name, LimitAge limitAge, Genre genre, Integer runningTime) {
		this.name = name;
		this.limitAge = limitAge;
		this.genre = genre;
		this.runningTime = runningTime;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LimitAge getLimitAge() {
		return limitAge;
	}

	public Genre getGenre() {
		return genre;
	}

	public Integer getRunningTime() {
		return runningTime;
	}

	public String getPosterImgLocation() {
		return posterImgLocation;
	}

	public void updatePosterImgLocation(String posterImgLocation) {
		this.posterImgLocation = posterImgLocation;
	}
}
