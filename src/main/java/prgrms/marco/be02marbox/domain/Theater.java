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
@Table(name = "theater")
public class Theater {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "region")
	private Region region;

	@Column(name = "name")
	private String name;
}
