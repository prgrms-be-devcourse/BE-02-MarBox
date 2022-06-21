package prgrms.marco.be02marbox.domain.theater;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "theater")
public class Theater {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "region")
	@Enumerated(value = EnumType.STRING)
	private Region region;

	@Column(name = "name")
	private String name;

	public Long getId() {
		return id;
	}
}
