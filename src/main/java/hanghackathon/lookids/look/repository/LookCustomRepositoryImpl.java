package hanghackathon.lookids.look.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghackathon.lookids.global.security.UserDetailsImpl;
import hanghackathon.lookids.likes.LikesRepository;
import hanghackathon.lookids.look.dto.LookResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static hanghackathon.lookids.global.constant.LookType.FAMILY;
import static hanghackathon.lookids.global.constant.LookType.KIDS;
import static hanghackathon.lookids.likes.QLikes.likes;
import static hanghackathon.lookids.look.QLook.look;

@Slf4j
@RequiredArgsConstructor
public class LookCustomRepositoryImpl implements LookCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final LikesRepository likesRepository;

    // slice + no offset
//    public Slice<LookResponseDto> paginationByRandom(String type, Long lastLookId, Pageable pageable, UserDetailsImpl userDetails) {
//        List<Look> looks = queryFactory
//                .selectFrom(look)
//                .where(checkType(type),
//                        ltLookId(lastLookId))
//                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
//                .limit(pageable.getPageSize() + 1)
//                .fetch();
//
//        // likeStatus
//        List<LookResponseDto> result = looks.stream().map((look) -> {
//            boolean isLiked = false;
//            if (userDetails != null) {
//                Long userId = userDetails.getUser().getId();
//                isLiked = likesRepository.getLikeStatusByUserAndLook(userId, look.getId()).orElse(false);
//            }
//            return LookResponseDto.of(look, isLiked);
//        }).toList();
//
//
//
//        return checkLastPage(pageable, result);
//    }

    public Slice<LookResponseDto> paginationByRandom(String type, Long lastLookId, Pageable pageable, UserDetailsImpl userDetails) {
        List<LookResponseDto> result = queryFactory
                .select(Projections.constructor(LookResponseDto.class,
                        look.lookType,
                        look.imageUrls,
                        look.title,
                        look.content,
                        likes.user.nickname,
                        look.likeCount,
                        likes.likeStatus.coalesce(false)))
                .from(likes)
                .leftJoin(likes.look)
                .where(checkType(type),
                        ltLookId(lastLookId))
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, result);
    }

    private BooleanExpression checkType(String type) {
        if (type.equals("KIDS")) {
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
