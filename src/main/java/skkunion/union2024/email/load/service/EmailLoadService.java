package skkunion.union2024.email.load.service;

import jakarta.mail.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailLoadService {

    @Value("${app.email}")
    private String EMAIL_ID;

    @Value("${app.password}")
    private String EMAIL_PASSWORD;

    private final String HOST = "imap.gmail.com";
    private final String MAIL_STORE_TYPE = "imap";
    private final Session emailSession;

    public void fetchEmails() throws MessagingException {

        Store store = null;
        Folder emailFolder = null;
        try {
            store = emailSession.getStore(MAIL_STORE_TYPE);
            store.connect(HOST, EMAIL_ID, EMAIL_PASSWORD);

            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();

            System.out.println("Total Messages: " + messages.length);

            for (Message message : messages) {
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            emailFolder.close(false);
            store.close();
        }

    }
}
