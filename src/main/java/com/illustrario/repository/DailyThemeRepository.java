package com.illustrario.repository;

import com.illustrario.model.DailyTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyThemeRepository extends JpaRepository<DailyTheme, Long> {

    Optional<DailyTheme> findByDate(LocalDate date);

    List<DailyTheme> findByDateBeforeOrderByDateDesc(LocalDate date);
}