package skkunion.union2024.email.load.service;

import jakarta.mail.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailLoadService {

    private final String HOST = "imap.gmail.com";
    private final String MAIL_STORE_TYPE = "imap";
    private final String USERNAME = "skkunion2024@gmail.com";
    private final String PASSWORD = "wckj jwdh syze mycm";
    private final Session emailSession;

    public void fetchEmails() throws MessagingException {

        Store store = null;
        Folder emailFolder = null;
        try {
            store = emailSession.getStore(MAIL_STORE_TYPE);
            store.connect(HOST, USERNAME, PASSWORD);

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
