package hanghackathon.lookids.look.repository;

import hanghackathon.lookids.look.Look;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LookRepository extends JpaRepository<Look, Long>, LookCustomRepository {
}
