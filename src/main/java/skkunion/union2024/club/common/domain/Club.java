package skkunion.union2024.club.common.domain;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import skkunion.union2024.board.domain.ClubBoard;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "club", indexes = {
        @Index(name = "idx_club_total_members", columnList = "totalMembers DESC"),
})
public class Club {

    @Id
    @Column(nullable = false, length = 20)
    private String slug;

    @OneToMany(mappedBy = "club", cascade = PERSIST, fetch = LAZY)
    private List<ClubMember> memberClubs = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = PERSIST, fetch = LAZY)
    private List<ClubBoard> clubBoards = new ArrayList<>();

    @Getter
    @Column(nullable = false, length = 20)
    private String clubName;

    @Column(nullable = false, length = 20)
    private String presidentName;

    private String description;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @Column(nullable = false)
    private Long totalMembers;

    public Club(final String slug, final String clubName, final String presidentName, final String description) {
        this.slug = slug;
        this.clubName = clubName;
        this.presidentName = presidentName;
        this.description = description;
        this.totalMembers = 1L;
    }

    public static Club of(final String slug, final String clubName, final String presidentName, final String description) {
        return new Club(slug, clubName, presidentName, description);
    }

}
