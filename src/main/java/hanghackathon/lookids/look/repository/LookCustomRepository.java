package hanghackathon.lookids.look.repository;

import hanghackathon.lookids.global.security.UserDetailsImpl;
import hanghackathon.lookids.look.dto.LookResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LookCustomRepository {

    Slice<LookResponseDto> paginationByRandom(String type, Long lastLookId, Pageable pageable, UserDetailsImpl userDetails);
}
