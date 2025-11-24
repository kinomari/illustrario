package com.illustrario.repository;

import com.illustrario.model.Like;
import com.illustrario.model.Art;
import com.illustrario.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndArt(User user, Art art);

    int countByArt(Art art);
}
