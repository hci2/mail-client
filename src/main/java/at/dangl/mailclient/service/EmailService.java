package at.dangl.mailclient.service;

import java.util.HashMap;
import java.util.Map;

public interface EmailService {

    void sendAsyncMail(long countEmailsSent, String name, String email, String subject, String textUnformatted, Map<String, byte[]> attachments);

    void sendSyncMail(long countEmailsSent, String name, String email, String subject, String textUnformatted, HashMap<String, byte[]> attachments);

    String getPossibleMailSenderFolders();
}
