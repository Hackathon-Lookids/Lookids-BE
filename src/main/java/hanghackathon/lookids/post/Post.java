package hanghackathon.lookids.post;

import hanghackathon.lookids.global.AuditingEntity;
import hanghackathon.lookids.global.config.StringListConverter;
import hanghackathon.lookids.global.constant.PostType;
import hanghackathon.lookids.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column(nullable = false, length = 1000)
    @Convert(converter = StringListConverter.class)
    private List<String> imageUrls;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String price;

    protected Post(User user, PostType postType, List<String> imageUrls, String title, String content, String location, String price) {
        this.user = user;
        this.postType = postType;
        this.imageUrls = imageUrls;
        this.title = title;
        this.content = content;
        this.location = location;
        this.price = price;
    }

    public static Post of(User user, PostType postType, List<String> imageUrls, String title, String content, String location, String price) {
        return new Post(user, postType, imageUrls, title, content, location, price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id != null && id.equals(((Post) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
