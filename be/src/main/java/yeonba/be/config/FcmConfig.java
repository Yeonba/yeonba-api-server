package yeonba.be.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class FcmConfig {

    @Value("${FCM_ACCOUNT_KEY_PATH}")
    private Resource accountKey;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {

        FileInputStream serviceAccount = new FileInputStream(accountKey.getFile());

        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

        return FirebaseMessaging.getInstance(FirebaseApp.initializeApp(firebaseOptions));
    }
}
