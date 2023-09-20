package hanghackathon.lookids.look;

import hanghackathon.lookids.global.ResponseDto;
import hanghackathon.lookids.look.dto.LookResponseDto;
import hanghackathon.lookids.look.repository.LookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LookService {

    private final LookRepository lookRepository;

    public ResponseDto<Slice<LookResponseDto>> getRandomLooks(String type, Long lastLookId) {

        Pageable pageable = PageRequest.ofSize(10);

        return ResponseDto.setSuccess(
                "조회 성공", lookRepository.paginationByRandom(type, lastLookId, pageable));
    }

}
