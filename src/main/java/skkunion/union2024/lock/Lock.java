package skkunion.union2024.lock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Lock {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

}
