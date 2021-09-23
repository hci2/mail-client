package at.dangl.mailclient.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface MailService {

    String sendMailsAsyncTo(MultipartFile mailRecipientsXlsXFile);

    String sendMailsSyncTo(MultipartFile mailRecipientsXlsXFile);

    String resetMailConfiguration();

    String changeSubjectTextConfiguration(String mailSubject, String mailText);

    HashMap<String, Boolean> addAttachments(List<MultipartFile> mailAttachments);

    String getMailConfiguration();

    String getMailSenderFolder();

    String setMailSenderFolder(String folderName);

    String getMailSenderPossibleFolders();

    String setAsyncExecutionConfiguration(long asyncMinSleepTime, long asyncMaxSleepTime);
}
