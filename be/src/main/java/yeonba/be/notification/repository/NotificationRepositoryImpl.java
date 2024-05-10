package yeonba.be.notification.repository;

import static yeonba.be.notification.entity.QNotification.notification;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public void readAllHasIdLessThanEqual(long notificationId) {

        queryFactory.update(notification)
            .set(notification.read, true)
            .where(notification.id.loe(notificationId))
            .execute();

        // 벌크성 수정 쿼리는 영속성 컨텍스트를 무시하기 때문에, 실행 후 컨텍스트 초기화 수행
        entityManager.flush();
        entityManager.clear();
    }
}
