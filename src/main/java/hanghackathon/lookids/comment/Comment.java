package hanghackathon.lookids.comment;

import hanghackathon.lookids.post.Post;
import hanghackathon.lookids.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    protected Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    public static Comment of(String content, User user, Post post) {
        return new Comment(content, user, post);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id != null && id.equals(((Comment) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
