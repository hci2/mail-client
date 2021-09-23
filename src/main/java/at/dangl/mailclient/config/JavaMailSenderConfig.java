package at.dangl.mailclient.config;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JavaMailSenderConfig {

    private String host;
    private String username;
    private String password;
    private int port;
    private String folderNameSent;
    private boolean smtpStarttlsEnable;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(getHost());
        mailSender.setPort(getPort());
        mailSender.setUsername(getUsername());
        mailSender.setPassword(getPassword());

        Properties javaMailProperties  = getJavaMailProperties();
        mailSender.setJavaMailProperties(javaMailProperties);

        Session session = getSession(javaMailProperties);
        mailSender.setSession(session);

        return mailSender;
    }

    private Properties getJavaMailProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", isSmtpStarttlsEnable());
        properties.put("mail.smtp.host", getHost());
        properties.put("mail.smtp.port", getPort());
        properties.put("mail.debug", "false");
        return properties;
    }

    private Session getSession(Properties javaMailProperties) {
        Session session = Session.getInstance(javaMailProperties);
        return session;
    }

}
