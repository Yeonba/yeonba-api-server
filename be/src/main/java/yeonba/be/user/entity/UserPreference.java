package yeonba.be.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users_preferences")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPreference {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int ageLowerBound;

	@Column(nullable = false)
	private int ageUpperBound;

	@Column(nullable = false)
	private int heightLowerBound;

	@Column(nullable = false)
	private int heightUpperBound;

	@Column(nullable = false)
	private String mbti;

	@Column(nullable = false)
	private String bodyType;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "vocal_range_id")
	private VocalRange vocalRange;

	@ManyToOne
	@JoinColumn(name = "area_id")
	private Area area;

	@ManyToOne
	@JoinColumn(name = "animal_id")
	private Animal animal;

	public UserPreference(
		int ageLowerBound,
		int ageUpperBound,
		int heightLowerBound,
		int heightUpperBound,
		String mbti,
		String bodyType,
		User user,
		VocalRange vocalRange,
		Area area,
		Animal animal) {
		this.ageLowerBound = ageLowerBound;
		this.ageUpperBound = ageUpperBound;
		this.heightLowerBound = heightLowerBound;
		this.heightUpperBound = heightUpperBound;
		this.mbti = mbti;
		this.bodyType = bodyType;
		this.user = user;
		this.vocalRange = vocalRange;
		this.area = area;
		this.animal = animal;
	}
}
