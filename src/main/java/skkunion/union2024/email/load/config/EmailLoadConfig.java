package skkunion.union2024.email.load.config;

import java.util.Properties;

import jakarta.mail.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailLoadConfig {

    @Bean
    public Session emailSession() {
        String host = "imap.gmail.com";

        // IMAP 서버에 연결할 세션 설정
        Properties properties = new Properties();
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.starttls.enable", "true");
        properties.put("mail.imap.ssl.enable", "true");

        return Session.getDefaultInstance(properties);
    }
}
