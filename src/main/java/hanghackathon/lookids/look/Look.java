package hanghackathon.lookids.look;

import hanghackathon.lookids.global.AuditingEntity;
import hanghackathon.lookids.global.constant.LookType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

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

    @Enumerated(EnumType.STRING)
    private LookType lookType;

    @Column(nullable = false)
    private String imageUrl;

    private String title;

    private String content;

    @ColumnDefault("0")
    private Integer likeCount;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id != null && id.equals(((Look) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }
}
