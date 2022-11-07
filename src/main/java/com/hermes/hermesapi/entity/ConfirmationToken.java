package com.hermes.hermesapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Random;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationToken {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	private LocalDate createdDate;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	public ConfirmationToken(User user) {
		this.user = user;
		this.createdDate = LocalDate.now();
		this.token = new DecimalFormat("000000").format(new Random().nextInt(999999));
	}
}
