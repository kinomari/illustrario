package com.illustrario.repository;

import com.illustrario.model.ThemeWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeWordRepository extends JpaRepository<ThemeWord, Long> {

    @Query("SELECT tw FROM ThemeWord tw WHERE tw.timesUsed = (SELECT MIN(tw2.timesUsed) FROM ThemeWord tw2)")
    List<ThemeWord> findLeastUsed();
}