package com.illustrario.service;

import com.illustrario.model.*;
import com.illustrario.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository repo;

    public boolean toggleLike(Art art, User user) {

        var existing = repo.findByUserAndArt(user, art);

        if (existing.isPresent()) {

            repo.delete(existing.get());
            return false;
        } else {
            
            repo.save(new Like(art, user));
            return true;
        }
    }

    public int countLikes(Art art) {
        return repo.countByArt(art);
    }
}
