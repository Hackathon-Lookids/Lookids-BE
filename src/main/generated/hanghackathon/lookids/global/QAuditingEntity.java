package hanghackathon.lookids.global;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditingEntity is a Querydsl query type for AuditingEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAuditingEntity extends EntityPathBase<AuditingEntity> {

    private static final long serialVersionUID = 587352467L;

    public static final QAuditingEntity auditingEntity = new QAuditingEntity("auditingEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public QAuditingEntity(String variable) {
        super(AuditingEntity.class, forVariable(variable));
    }

    public QAuditingEntity(Path<? extends AuditingEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditingEntity(PathMetadata metadata) {
        super(AuditingEntity.class, metadata);
    }

}

