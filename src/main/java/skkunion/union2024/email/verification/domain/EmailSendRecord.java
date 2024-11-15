package skkunion.union2024.email.verification.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "email_send_record")
public class EmailSendRecord {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "email_send_record_id")
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer count;

    private EmailSendRecord(String email) {
        this.email = email;
        this.count = 1;
    }

    public static EmailSendRecord of(String email) {
        return new EmailSendRecord(email);
    }
}
