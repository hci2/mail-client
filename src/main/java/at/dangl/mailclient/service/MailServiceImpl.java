package at.dangl.mailclient.service;

import at.dangl.mailclient.config.JavaMailSenderConfig;
import at.dangl.mailclient.util.Const;
import at.dangl.mailclient.util.MailProperty;
import com.google.common.collect.ArrayListMultimap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static at.dangl.mailclient.util.Const.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final ExcelParserService excelParserService;

    private final EmailService emailService;

    private final JavaMailSenderConfig javaMailSenderConfig;

    @Override
    public String sendMailsAsyncTo(MultipartFile mailRecipientsXlsXFile) {
        StringBuilder resultSentEmails = new StringBuilder();

        ArrayListMultimap<String, String> mailRecipients = excelParserService.getAndExtractMailRecipientsFrom(mailRecipientsXlsXFile);

        long countEmailsSent = 0;
        resultSentEmails.append(Const.DISPLAY_SUMMARY_WITH_EMPTY_ROW);
        for (Map.Entry<String, String> entry : mailRecipients.entries()) {
            ++countEmailsSent;
            String name = entry.getKey();
            String email = entry.getValue();
            emailService.sendAsyncMail(countEmailsSent, name, email, MailProperty.SUBJECT, MailProperty.TEXT, MailProperty.ATTACHEMENTS);
            asyncRandomSleep(countEmailsSent);
        }
        resultSentEmails.append(Const.DISPLAY_SENT_COUNT + countEmailsSent + Const.DISPLAY_NEW_LINE);
        return resultSentEmails.toString();
    }

    @Override
    public String sendMailsSyncTo(MultipartFile mailRecipientsXlsXFile) {
        StringBuilder resultSentEmails = new StringBuilder();

        ArrayListMultimap<String, String> mailRecipients = excelParserService.getAndExtractMailRecipientsFrom(mailRecipientsXlsXFile);

        long countEmailsSent = 0;
        resultSentEmails.append(Const.DISPLAY_SUMMARY_WITH_EMPTY_ROW);
        for (Map.Entry<String, String> entry : mailRecipients.entries()) {
            ++countEmailsSent;
            String name = entry.getKey();
            String email = entry.getValue();
            emailService.sendSyncMail(countEmailsSent, name, email, MailProperty.SUBJECT, MailProperty.TEXT, MailProperty.ATTACHEMENTS);
        }
        resultSentEmails.append(Const.DISPLAY_SENT_COUNT + countEmailsSent + Const.DISPLAY_NEW_LINE);
        return resultSentEmails.toString();
    }

    @Override
    public String resetMailConfiguration() {
        MailProperty.SUBJECT = MailProperty.DEFAULT_SUBJECT;
        MailProperty.TEXT = MailProperty.DEFAULT_TEXT;
        MailProperty.ATTACHEMENTS = MailProperty.DEFAULT_ATTACHEMENTS;
        return "Successfully reset mail configuration.";
    }

    @Override
    public String changeSubjectTextConfiguration(String mailSubject, String mailText) {
        if (mailSubject == null) {
            MailProperty.SUBJECT = MailProperty.DEFAULT_SUBJECT;
        } else {
            MailProperty.SUBJECT = mailSubject;
        }
        if (mailText == null) {
            MailProperty.TEXT = MailProperty.DEFAULT_TEXT;
        } else {
            MailProperty.TEXT = mailText;
        }
        log.info("Successfully changed subject and text configuration with subject: {}, text: {}", MailProperty.SUBJECT, MailProperty.TEXT, MailProperty.ATTACHEMENTS);
        return "Successfully changed subject and text configuration.";
    }

    @Override
    public HashMap<String, Boolean> addAttachments(List<MultipartFile> mailAttachments) {
        HashMap<String, Boolean> resultAddAttachments = new HashMap<>();
        for (MultipartFile mailAttachment : mailAttachments) {
            try {
                MailProperty.ATTACHEMENTS.put(mailAttachment.getOriginalFilename(), mailAttachment.getBytes());
                log.info("Adding of attachment: " + mailAttachment.getOriginalFilename() + "was successfully: {}", true);
                resultAddAttachments.put("Adding of attachment: " + mailAttachment.getOriginalFilename() + " was successfully: ", true);
            } catch (IOException e) {
                log.error("Adding of attachment: " + mailAttachment.getOriginalFilename() + "was successfully: {}", false);
                resultAddAttachments.put("Adding of attachment: " + mailAttachment.getOriginalFilename() + " was successfully: ", false);
            }
        }

        return resultAddAttachments;
    }

    @Override
    public String getMailConfiguration() {
        String mailConfiguration;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DISPLAY_SUMMARY_WITH_EMPTY_ROW);
        stringBuilder.append(DISPLAY_FROM + javaMailSenderConfig.getUsername() + DISPLAY_NEW_LINE);
        stringBuilder.append(DISPLAY_SUBJECT + MailProperty.SUBJECT + DISPLAY_NEW_LINE);
        stringBuilder.append(DISPLAY_TEXT + MailProperty.TEXT + DISPLAY_NEW_LINE);
        for (Map.Entry<String, byte[]> entry : MailProperty.ATTACHEMENTS.entrySet()) {
            stringBuilder.append(DISPLAY_ATTACHMENTS + entry.getKey() + DISPLAY_NEW_LINE);
        }
        mailConfiguration = stringBuilder.toString();
        return mailConfiguration;
    }

    @Override
    public String getMailSenderPossibleFolders() {
        String foldersDisplay = emailService.getPossibleMailSenderFolders();
        return foldersDisplay;
    }

    @Override
    public String setAsyncExecutionConfiguration(long asyncMinSleepTime, long asyncMaxSleepTime) {
        Const.ASYNC_SLEEP_INTERVAL_START = asyncMinSleepTime;
        Const.ASYNC_SLEEP_INTERVAL_END = asyncMaxSleepTime;
        log.info("Successfully set async execution interval between: [{},{}]", asyncMinSleepTime, asyncMaxSleepTime);
        return "Successfully set async execution interval between: [{" + asyncMinSleepTime + "},{" + asyncMaxSleepTime + "}] mS";
    }

    @Override
    public String getMailSenderFolder() {
        String folderNameSent = Const.DISPLAY_SUMMARY_WITH_EMPTY_ROW;
        folderNameSent += Const.DISPLAY_FOLDER + javaMailSenderConfig.getFolderNameSent();
        return folderNameSent;
    }

    @Override
    public String setMailSenderFolder(String folderName) {
        javaMailSenderConfig.setFolderNameSent(folderName);
        return "Successfully set mail sender folder for the copies to: " + javaMailSenderConfig.getFolderNameSent();
    }

    private void asyncRandomSleep(long countEmailsSent) {
        try {
            long sleepDuration = ThreadLocalRandom.current().nextLong(Const.ASYNC_SLEEP_INTERVAL_START, Const.ASYNC_SLEEP_INTERVAL_END);
            log.debug("[{}] Async sleep duration in mS before next email sending call: {}", countEmailsSent, sleepDuration);
            Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
            log.warn("[{}] Exception occurred during async sleep, exception: {}", countEmailsSent, e.getMessage());
        }
    }

}
