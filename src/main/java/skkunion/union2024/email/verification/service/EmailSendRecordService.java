package skkunion.union2024.email.verification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.email.verification.domain.EmailSendRecord;
import skkunion.union2024.email.verification.domain.repository.EmailSendRecordRepository;
import skkunion.union2024.member.service.MemberService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailSendRecordService {

    private final MemberService memberService;
    private final EmailSendRecordRepository emailSendRecordRepository;

    @Transactional
    public void recordSaveOrIncreaseCount(String email) {
        Optional<EmailSendRecord> findRecord = emailSendRecordRepository.findByEmail(email);
        if (findRecord.isPresent()) {
            if (findRecord.get().getCount() == 5) {
                emailSendRecordRepository.delete(findRecord.get());
                memberService.completeDeleteByEmail(email);
            } else {
                emailSendRecordRepository.increaseCount(email);
            }
        }

        emailSendRecordRepository.save(EmailSendRecord.of(email));
    }

    @Transactional
    public void deleteRecordByEmail(String email) {
        emailSendRecordRepository.deleteByEmail(email);
    }
}
