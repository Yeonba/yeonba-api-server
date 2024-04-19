package yeonba.be.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import yeonba.be.notification.dto.response.NotificationResponse;

public interface NotificationRepositoryCustom {

    Page<NotificationResponse> findBy(long receiverId, PageRequest pageRequest);
}
