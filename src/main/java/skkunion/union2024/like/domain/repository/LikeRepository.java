package skkunion.union2024.like.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skkunion.union2024.like.domain.BoardLike;

@Repository
public interface LikeRepository extends JpaRepository<BoardLike, Long> {
}
