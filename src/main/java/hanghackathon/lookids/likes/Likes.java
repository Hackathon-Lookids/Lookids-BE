package hanghackathon.lookids.likes;

import hanghackathon.lookids.look.Look;
import hanghackathon.lookids.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ColumnDefault("false")
    private boolean likeStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "look_id", nullable = false)
    private Look look;

    protected Likes(boolean likeStatus, User user, Look look) {
        this.likeStatus = likeStatus;
        this.user = user;
        this.look = look;
    }

    public static Likes of(boolean likeStatus, User user, Look look) {
        return new Likes(likeStatus, user, look);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id != null && id.equals(((Likes) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
