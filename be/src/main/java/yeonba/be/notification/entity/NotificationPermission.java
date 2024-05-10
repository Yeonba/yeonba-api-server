package yeonba.be.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yeonba.be.notification.enums.NotificationType;
import yeonba.be.user.entity.User;

@Table(name = "notifications_permissions")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean permissionStatus;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public NotificationPermission(boolean permissionStatus, NotificationType type, User user) {

        this.permissionStatus = permissionStatus;
        this.type = type;
        this.user = user;
    }

    public boolean getPermissionStatus() {

        return permissionStatus;
    }

    public void updatePermissionStatus(boolean permissionStatus) {

        this.permissionStatus = permissionStatus;
    }

    public boolean hasSameTypeAs(NotificationType type) {

        return this.type.equals(type);
    }
}
