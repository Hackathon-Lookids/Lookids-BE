package hanghackathon.lookids.look;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLook is a Querydsl query type for Look
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLook extends EntityPathBase<Look> {

    private static final long serialVersionUID = 898652300L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLook look = new QLook("look");

    public final hanghackathon.lookids.global.QAuditingEntity _super = new hanghackathon.lookids.global.QAuditingEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<String, StringPath> imageUrls = this.<String, StringPath>createList("imageUrls", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final EnumPath<hanghackathon.lookids.global.constant.LookType> lookType = createEnum("lookType", hanghackathon.lookids.global.constant.LookType.class);

    public final StringPath title = createString("title");

    public final hanghackathon.lookids.user.QUser user;

    public QLook(String variable) {
        this(Look.class, forVariable(variable), INITS);
    }

    public QLook(Path<? extends Look> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLook(PathMetadata metadata, PathInits inits) {
        this(Look.class, metadata, inits);
    }

    public QLook(Class<? extends Look> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new hanghackathon.lookids.user.QUser(forProperty("user")) : null;
    }

}

