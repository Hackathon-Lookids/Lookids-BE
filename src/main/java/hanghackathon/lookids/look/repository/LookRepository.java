package hanghackathon.lookids.look.repository;

import hanghackathon.lookids.global.constant.LookType;
import hanghackathon.lookids.look.Look;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookRepository extends JpaRepository<Look, Long>, LookCustomRepository {

    List<Look> findTop10ByLookTypeOrderByLikeCountDesc(LookType lookType);
}
