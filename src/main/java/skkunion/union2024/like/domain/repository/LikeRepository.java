package skkunion.union2024.like.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skkunion.union2024.like.domain.UserLike;

@Repository
public interface LikeRepository extends JpaRepository<UserLike, Long> {
}
