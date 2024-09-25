package skkunion.union2024.board.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.board.domain.ClubBoard;

import java.util.List;

public interface ClubBoardRepository extends JpaRepository<ClubBoard, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ClubBoard set likes = likes + 1 WHERE id = :clubBoardId")
    void increaseLikes(@Param("clubBoardId") Long clubBoardId);

    @Modifying
    @Transactional
    @Query("UPDATE ClubBoard set likes = likes - 1 WHERE id = :clubBoardId")
    void decreaseLikes(@Param("clubBoardId") Long clubBoardId);
}
