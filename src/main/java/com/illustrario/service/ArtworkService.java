package com.illustrario.service;

import com.illustrario.dto.ArtworkUploadDto;
import com.illustrario.model.Artwork;
import com.illustrario.model.User;
import com.illustrario.repository.ArtworkRepository;
import com.illustrario.repository.CommentRepository;
import com.illustrario.repository.LikeRepository;
import com.illustrario.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ArtworkService {
    private static final Logger log = LoggerFactory.getLogger(ArtworkService.class);
    private static final int PUBLIC_LIST_SIZE = 60;

    private final ArtworkRepository artworkRepository;
    private final SupabaseStorageService supabaseStorageService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public ArtworkService(ArtworkRepository artworkRepository,
                          SupabaseStorageService supabaseStorageService,
                          UserRepository userRepository,
                          CommentRepository commentRepository,
                          LikeRepository likeRepository) {
        this.artworkRepository = artworkRepository;
        this.supabaseStorageService = supabaseStorageService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    @Transactional
    public Artwork upload(ArtworkUploadDto dto, String currentTheme, String userEmail) throws IOException {
        String filePath = supabaseStorageService.save(dto.getImageFile());
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        Artwork artwork = new Artwork(dto.getTitle(), user.getNickname(),
            dto.getImageFile().getOriginalFilename(), filePath, currentTheme, dto.getDescription());
        artwork.setUser(user);
        return artworkRepository.save(artwork);
    }

    public List<Artwork> getRecentArtworks() { return artworkRepository.findTop12ByOrderByUploadedAtDesc(); }
    public Page<Artwork> getRecentArtworksPage(int page, int size) {
        return artworkRepository.findByRemovedFalseOrderByUploadedAtDesc(PageRequest.of(page, size));
    }
    public List<Artwork> getArtworksByTheme(String theme) { return artworkRepository.findByThemeOrderByUploadedAtDesc(theme); }
    public List<Artwork> getArtworksByUser(User user) { return artworkRepository.findByUserOrderByUploadedAtDesc(user); }
    public List<Artwork> getPublicArtworksByTheme(String theme) {
        return artworkRepository
            .findByThemeAndRemovedFalseOrderByUploadedAtDesc(theme, PageRequest.of(0, PUBLIC_LIST_SIZE))
            .getContent();
    }
    public List<Artwork> getPublicArtworksByUser(User user) {
        return artworkRepository
            .findByUserAndRemovedFalseOrderByUploadedAtDesc(user, PageRequest.of(0, PUBLIC_LIST_SIZE))
            .getContent();
    }
    public List<Artwork> searchPublicArtworks(String query) {
        return artworkRepository
            .findByRemovedFalseAndTitleContainingIgnoreCaseOrRemovedFalseAndDescriptionContainingIgnoreCase(
                query,
                query,
                PageRequest.of(0, PUBLIC_LIST_SIZE)
            )
            .getContent();
    }

    public List<Artwork> findAllWithUser() {
        return artworkRepository.findAllWithUserOrderByUploadedAtDesc();
    }

    public Optional<Artwork> findByIdWithUser(Long id) {
        return artworkRepository.findByIdWithUser(id);
    }

    public Optional<Artwork> findById(Long id) { return artworkRepository.findById(id); }
    public List<Artwork> findAll() { return artworkRepository.findAllByOrderByUploadedAtDesc(); }

    @Transactional
    public void softDeleteByOwner(Long artworkId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        Artwork artwork = artworkRepository.findById(artworkId)
            .orElseThrow(() -> new IllegalArgumentException("Obra não encontrada"));
        if (!artwork.getUser().getId().equals(user.getId()) && !user.getRole().equals("ROLE_CURATOR"))
            throw new SecurityException("Sem permissão.");
        artwork.setRemoved(true);
        artworkRepository.save(artwork);
    }

    @Transactional
    public void softDeleteByOwnerAdmin(Long artworkId) {
        artworkRepository.findById(artworkId).ifPresent(a -> {
            a.setRemoved(true);
            artworkRepository.save(a);
        });
    }

    @Transactional
    public void restore(Long id) {
        artworkRepository.findById(id).ifPresent(a -> { a.setRemoved(false); artworkRepository.save(a); });
    }

    @Transactional
    public void hardDelete(Long id) {
        artworkRepository.findById(id).ifPresent(a -> {
            String filePath = a.getFilePath();
            likeRepository.deleteByArtwork(a);
            commentRepository.deleteByArtwork(a);
            artworkRepository.delete(a);
            try {
                supabaseStorageService.deleteByPublicUrl(filePath);
            } catch (Exception e) {
                log.warn("Obra removida do banco, mas houve falha ao apagar arquivo no Supabase. artworkId={}", id, e);
            }
        });
    }
}