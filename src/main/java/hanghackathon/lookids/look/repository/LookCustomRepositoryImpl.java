package hanghackathon.lookids.look.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghackathon.lookids.look.dto.LookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static hanghackathon.lookids.global.constant.LookType.FAMILY;
import static hanghackathon.lookids.global.constant.LookType.KIDS;
import static hanghackathon.lookids.look.QLook.look;

@RequiredArgsConstructor
public class LookCustomRepositoryImpl implements LookCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<LookResponseDto> paginationByRandom(String type, Long lastLookId, Pageable pageable) {
        List<LookResponseDto> result = queryFactory
                .select(Projections.constructor(LookResponseDto.class, look))
                .from(look)
                .where(checkType(type),
                        ltLookId(lastLookId))
                .orderBy(NumberExpression.random().asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, result);
    }

    private BooleanExpression checkType(String type) {
        if (type.equals("kids")) {
            return look.lookType.eq(KIDS);
        } else {
            return look.lookType.eq(FAMILY);
        }
    }

    // no-offset condition
    private BooleanExpression ltLookId(Long lookId) {
        if (lookId == null) {
            return null;
        }
        return look.id.lt(lookId);
    }

    private Slice<LookResponseDto> checkLastPage(Pageable pageable, List<LookResponseDto> result) {

        boolean hasNext = false;

        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(result, pageable, hasNext);
    }
}
