package com.fypembeddingapplication.embeddingapplication.database;

import com.fypembeddingapplication.embeddingapplication.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
    Optional<ConfirmationToken> findAllByConfirmatinToken (String confirmatinToken);
}
