package yeonba.be.user.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import yeonba.be.util.S3Service;

@Component
@RequiredArgsConstructor
public class ProfilePhotoDeletionListener {

    private final S3Service s3Service;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onProfilePhotoDeletion(ProfilePhotoDeletionEvent event) {

        s3Service.deleteProfilePhotos(event.getProfilePhotos());
    }
}
