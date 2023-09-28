package hanghackathon.lookids.look;

import hanghackathon.lookids.global.AuditingEntity;
import hanghackathon.lookids.global.config.StringListConverter;
import hanghackathon.lookids.global.constant.LookType;
import hanghackathon.lookids.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Entity
@ToString
@Table(indexes = {
        @Index(columnList = "likeCount")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Look extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private LookType lookType;

    @Column(nullable = false, length = 1500)
    @Convert(converter = StringListConverter.class)
    private List<String> imageUrls;

    private String title;

    private String content;

    @Setter
    private Integer likeCount;

    @Builder
    protected Look(User user, LookType lookType, List<String> imageUrls, String title, String content) {
        this.user = user;
        this.lookType = lookType;
        this.imageUrls = imageUrls;
        this.title = title;
        this.content = content;
        likeCount = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id != null && id.equals(((Look) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
