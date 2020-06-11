package com.fypembeddingapplication.embeddingapplication.database;
import java.util.Optional;
import com.fypembeddingapplication.embeddingapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(Long id);
}
