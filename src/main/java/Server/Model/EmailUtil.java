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
        final String fromEmail = "gwent29.2024@gmail.com";
        final String password = "jfsw dthp xfke fuzc\n";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

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
        int code = new Random().nextInt(900000) + 100000;
        sendEmail(email, "Verification Code", "Your verification code is: " + code + "\n or go to following link\n" +
                "www.chert.com");
        emailVerificationCodes.put(email, code);
    }

    public static boolean verifyCode(String email, int code) {
        return emailVerificationCodes.get(email) == code;
    }
}