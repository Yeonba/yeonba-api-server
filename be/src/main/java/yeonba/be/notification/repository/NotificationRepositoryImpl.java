package yeonba.be.notification.repository;

import static yeonba.be.notification.entity.QNotification.notification;
import static yeonba.be.user.entity.QProfilePhoto.profilePhoto;
import static yeonba.be.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import yeonba.be.notification.dto.response.NotificationResponse;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NotificationResponse> findBy(long receiverId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<NotificationResponse> content = queryFactory.select(
                Projections.constructor(NotificationResponse.class,
                    notification.type.stringValue(),
                    notification.content,
                    user.id,
                    profilePhoto.photoUrl,
                    user.name,
                    notification.createdAt,
                    notification.read
                ))
            .from(notification)
            .innerJoin(notification.creator, user)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                notification.receiver.id.eq(receiverId),
                profilePhoto.id.eq(
                    JPAExpressions.select(profilePhoto.id.min())
                        .from(profilePhoto)
                        .where(profilePhoto.user.id.eq(user.id))
                ))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(notification.count())
            .from(notification)
            .where(notification.receiver.id.eq(receiverId));

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }
}
