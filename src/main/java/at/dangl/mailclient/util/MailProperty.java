package at.dangl.mailclient.util;

import java.util.HashMap;

public class MailProperty {

    //    public static final String DEFAULT_SUBJECT = "Einladung Weltpremiere";
    public static final String DEFAULT_SUBJECT = "Premiere, Gartenbaukino, 5.Mai, 14.00 Uhr";
    public static String SUBJECT = DEFAULT_SUBJECT;

    //    public static final String DEFAULT_TEXT = "<html><body>Hallo %s,<br><br> ich lade dich zur Weltpremiere am 5.Mail um 14 Uhr im Gartenbaukino ein. Es ist freier Eintritt für geladene Gäste. Die Anmeldung bitte per E-Mail durchführen. <br><br> Trailer: https://www.youtube.com/watch?v=NrxPn-0grFA&feature=youtu.be <br><br> Liebe Grüße</body></html>";
//public static final String DEFAULT_TEXT = "<html><body>Einladung zur Weltpremiere am 5. Mai um 14 Uhr im Gartenbaukino <br>Freier Eintritt für geladene Gäste, Anmeldung bitte per E-Mail<br><br> Trailer: https://www.youtube.com/watch?v=NrxPn-0grFA&feature=youtu.be<br><br></body></html>";
    public static final String DEFAULT_TEXT = "<html><body>EINLADUNG ZUR WELTPREMIERE AM 5. MAI UM 14 UHR IM GARTENBAUKINO<br>FREIER EINTRITT FÜR GELADENE GÄSTE, ANMELDUNG BITTE PER E-MAIL<br><br> TRAILER: https://www.youtube.com/watch?v=NrxPn-0grFA&feature=youtu.be<br><br></body></html>";
    public static String TEXT = DEFAULT_TEXT;

    public static final HashMap<String, byte[]> DEFAULT_ATTACHEMENTS = new HashMap<>();
    public static HashMap<String, byte[]> ATTACHEMENTS = DEFAULT_ATTACHEMENTS;

    private MailProperty() {

    }

}
