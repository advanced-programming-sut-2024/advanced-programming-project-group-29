package Server.Model;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
    private static HashMap<String, Integer> emailVerificationCodes = new HashMap<>();

    public static void sendEmail(String toEmail, String subject, String body) {
        final String fromEmail = "gwent29.2024@gmail.com"; // requires valid email id
        final String password = "jfsw dthp xfke fuzc\n"; // correct password for your email id

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true"); // enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail, false));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
            System.out.println("Email Sent Successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void generateAndSendVerificationCode(String email) {
      //  int code = new Random().nextInt(900000) + 100000;
        int code = 111111;
        //TODO randomize the code
        sendEmail(email, "Verification Code", "Your verification code is: " + code);
        emailVerificationCodes.put(email, code);
    }

    public static boolean verifyCode(String email, int code) {
        return emailVerificationCodes.get(email) == code;
    }
}