package caner.blog.repository;

import caner.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long id);

    Page<Post> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Post> findAllByUserId(Long id, Pageable pageable);
}
