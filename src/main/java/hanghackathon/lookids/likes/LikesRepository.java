package hanghackathon.lookids.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByUserIdAndLookId(Long userId, Long lookId);

    @Query(value = "SELECT COALESCE(l.likeStatus, false) FROM Likes l WHERE l.user.id = :userId AND l.look.id = :lookId")
    Optional<Boolean> getLikeStatusByUserAndLook(@Param("userId") Long userId, @Param("lookId") Long lookId);
}
