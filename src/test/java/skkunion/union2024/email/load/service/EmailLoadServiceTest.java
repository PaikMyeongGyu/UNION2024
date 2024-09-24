package skkunion.union2024.email.load.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailLoadServiceTest {

    @Autowired
    EmailLoadService emailLoadService;

    @Test
    void 이메일_확인해보기() throws MessagingException {
        emailLoadService.fetchEmails();
    }
}