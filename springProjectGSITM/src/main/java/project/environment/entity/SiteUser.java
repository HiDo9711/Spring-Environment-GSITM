package project.environment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String regionNum;

	@Column(unique = true)
	private String loginId;

	@Column(unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

}