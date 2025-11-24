package com.illustrario.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.illustrario.model.DailyTheme;

@Repository
public interface DailyThemeRepository extends JpaRepository<DailyTheme, Long> {
  Optional<DailyTheme> findByDate(LocalDate date);
  Optional<DailyTheme> findTopByOrderByDateDesc();
}
