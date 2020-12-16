package com.fypembeddingapplication.embeddingapplication.database;

import com.fypembeddingapplication.embeddingapplication.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
}
