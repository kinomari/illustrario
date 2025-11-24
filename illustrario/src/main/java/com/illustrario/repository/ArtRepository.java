package com.illustrario.repository;

import com.illustrario.model.Art;
import com.illustrario.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ArtRepository extends JpaRepository<Art, Long> {

    List<Art> findTop20ByOrderByCreatedAtDesc();

    List<Art> findByAuthor(User user);

    List<Art> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
