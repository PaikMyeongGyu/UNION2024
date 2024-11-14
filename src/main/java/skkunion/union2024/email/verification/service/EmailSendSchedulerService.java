package skkunion.union2024.email.verification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import skkunion.union2024.email.verification.domain.EmailSendRecord;

import java.util.List;

import static skkunion.union2024.global.util.PageParameterUtils.SCHEDULE_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class EmailSendSchedulerService {
    private final EmailSendRecordService emailSendRecordService;

    @Scheduled(cron = "0 0 0 * * *")
    public void executePagingEmailSendRecord() {

        List<EmailSendRecord> emailSendRecords = null;
        do {
            if (emailSendRecords == null) {
                emailSendRecords = emailSendRecordService.emailSendRecordsByCursor(null);
            } else {
                Long lastRecordId = emailSendRecords.get(emailSendRecords.size() - 1).getId();
                emailSendRecords = emailSendRecordService.emailSendRecordsByCursor(lastRecordId);
            }
            emailSendRecords.forEach(emailSendRecord -> {
               emailSendRecordService.recordSaveOrIncreaseCount(emailSendRecord.getEmail());
            });
        } while (emailSendRecords.size() >= SCHEDULE_PAGE_SIZE);

    }
}
