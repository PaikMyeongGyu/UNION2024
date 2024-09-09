package skkunion.union2024.group.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import skkunion.union2024.board.domain.ClubBoard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Club {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @OneToMany(mappedBy = "club", cascade = PERSIST, fetch = LAZY)
    private List<MemberClub> memberClubs = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = PERSIST, fetch = LAZY)
    private List<ClubBoard> clubBoards = new ArrayList<>();

    @Column(nullable = false, length = 20)
    private String clubName;

    @Column(nullable = false, length = 20)
    private String presidentName;

    private String description;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
