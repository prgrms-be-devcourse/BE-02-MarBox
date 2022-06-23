package prgrms.marco.be02marbox.domain.theater;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "theater")
public class Theater {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Column(name = "region", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Region region;

	@NotBlank
	@Column(name = "name", nullable = false)
	private String name;

	public Long getId() {
		return id;
	}
  
	protected Theater() {
	}

	public Theater(Region region, String name) {
		this.region = region;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public Region getRegion() {
		return region;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Theater)) {
			return false;
		}

		Theater theater = (Theater)object;

		return new EqualsBuilder().append(getId(), theater.getId())
			.append(getRegion(), theater.getRegion())
			.append(getName(), theater.getName())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).append(getRegion()).append(getName()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("region", region)
			.append("name", name)
			.toString();
	}
}
