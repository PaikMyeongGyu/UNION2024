package skkunion.union2024.email.verification.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import skkunion.union2024.email.verification.domain.EmailSendRecord;

import java.util.List;

import static skkunion.union2024.email.verification.domain.QEmailSendRecord.emailSendRecord;
import static skkunion.union2024.global.util.PageParameterUtils.SCHEDULE_PAGE_SIZE;

@Repository
@RequiredArgsConstructor
public class EmailSendRecordQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<EmailSendRecord> findEmailRecordsById(Long recordId) {
        BooleanBuilder dynamicLtId = new BooleanBuilder();

        if (recordId != null) {
            dynamicLtId.and(emailSendRecord.id.lt(recordId));
        }

        return queryFactory
                .selectFrom(emailSendRecord)
                .where(dynamicLtId)
                .orderBy(emailSendRecord.id.asc())
                .limit(SCHEDULE_PAGE_SIZE)
                .fetch();
    }
}
