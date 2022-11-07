package com.hermes.hermesapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hermes.hermesapi.entity.ConfirmationToken;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {

	Optional<ConfirmationToken> findConfirmationTokenByToken(String token);
}
