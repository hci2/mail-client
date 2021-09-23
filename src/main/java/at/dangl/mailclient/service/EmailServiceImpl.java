package at.dangl.mailclient.service;

import at.dangl.mailclient.config.JavaMailSenderConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static at.dangl.mailclient.util.Const.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmailServiceImpl implements EmailService {

    private final JavaMailSenderImpl javaMailSender;

    private final JavaMailSenderConfig javaMailSenderConfig;

    private final String PROTOCOL_IMAP = "imap";

    @PostConstruct
    private void initializePropertiesAndSessionForSending() {
        logAndGetPossibleMailSenderFolders(javaMailSender.getSession());
    }

    @Async
    @Override
    public void sendAsyncMail(long countEmailsSent, String name, String email, String subject, String textUnformatted, Map<String, byte[]> attachments) {
        String text = formatEmailTextWithName(textUnformatted, name);
        sendMimeMessage(countEmailsSent, email, subject, text, attachments);
    }

    @Override
    public void sendSyncMail(long countEmailsSent, String name, String email, String subject, String textUnformatted, HashMap<String, byte[]> attachments) {
        String text = formatEmailTextWithName(textUnformatted, name);
        sendMimeMessage(countEmailsSent, email, subject, text, attachments);
    }

    @Override
    public String getPossibleMailSenderFolders() {
        String foldersDisplay = logAndGetPossibleMailSenderFolders(javaMailSender.getSession());
        return foldersDisplay;
    }

    private String formatEmailTextWithName(String textUnformatted, String name) {
        String textFormated = String.format(textUnformatted, name);
        return textFormated;

    }

    private Map.Entry<String, Boolean> sendSimpleMessage(
            String to, String subject, String text) {
        Map.Entry<String, Boolean> resultSentEmail;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(javaMailSender.getUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
            resultSentEmail = new AbstractMap.SimpleEntry<>("E-Mail sending to: " + to + " was successfully: ", true);
            return resultSentEmail;
        } catch (MailException e) {
            log.error("Could not send email with exception: ", e);
            resultSentEmail = new AbstractMap.SimpleEntry<>("E-Mail sending to: " + to + " was successfully: ", false);
            return resultSentEmail;
        }
    }

    private boolean sendMimeMessage(
            long countEmailsSent, String to, String subject, String text, Map<String, byte[]> attachments) {
        boolean isSent = false;

        MimeMessage mimeMessage = new MimeMessage(javaMailSender.getSession());
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessage.setFrom(javaMailSender.getUsername());
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
            for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {
                ByteArrayResource byteArrayResource = new ByteArrayResource(entry.getValue());
                mimeMessageHelper.addAttachment(entry.getKey(), byteArrayResource);
            }
            javaMailSender.send(mimeMessage);
            log.info("[{}] Sending mail to: {}", countEmailsSent, to);
            addEmailToSentFolder(countEmailsSent, mimeMessage, javaMailSender.getSession());
            isSent = true;
        } catch (Exception e) {
            log.error("[{}] Could not send email with exception: ", countEmailsSent, e);
            isSent = false;
        }
        return isSent;
    }

    private void addEmailToSentFolder(long countEmailsSent, MimeMessage mimeMessage, Session session) {
        try {
            Store store = session.getStore(PROTOCOL_IMAP);
            store.connect(javaMailSender.getHost(), javaMailSender.getUsername(), javaMailSender.getPassword());
            Folder folderSent = store.getFolder(javaMailSenderConfig.getFolderNameSent());
            folderSent.open(Folder.READ_WRITE);
            mimeMessage.setFlag(Flags.Flag.SEEN, true);
            folderSent.appendMessages(new Message[]{mimeMessage});
            store.close();
            log.info("[{}] Saved email to sender's sent folder: {}", countEmailsSent, folderSent);
        } catch (Exception e) {
            log.error("[{}] Exception occurred during saving email to sender's sent folder", countEmailsSent, e);
        }
    }

    private String logAndGetPossibleMailSenderFolders(Session session) {
        StringBuilder foldersDisplay = new StringBuilder();
        try {
            Store store = session.getStore(PROTOCOL_IMAP);
            store.connect(javaMailSender.getHost(), javaMailSender.getUsername(), javaMailSender.getPassword());
            Folder folderRoot = store.getDefaultFolder();
            Folder[] folders = folderRoot.list();
            foldersDisplay.append(DISPLAY_SUMMARY_WITH_EMPTY_ROW);
            for (Folder f : folders) {
                String folderMessage = DISPLAY_FOLDER + f + DISPLAY_NEW_LINE;
                foldersDisplay.append(folderMessage);
                log.info("Found folder of sender's email account: {}", f);
            }
            store.close();
        } catch (Exception e) {
            log.error("Exception occurred during showing sender's folders", e);
        }
        return foldersDisplay.toString();
    }
}
