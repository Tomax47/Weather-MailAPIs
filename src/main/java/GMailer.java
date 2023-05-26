import java.io.File;
import java.time.LocalDateTime;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class GMailer {
    public static void main(String[] args) {

        String host = "smtp.gmail.com";
        String port = "587";
        String username = "Usr@gmail.com";
        String password = "psw";

        String senderEmail = "myMail@gmail.com";
        String recipientEmail = "Reviever@kpfu.ru";
        String subject = "11-200 Dabat47";
        String messageText = "Hello, This is a test email.";
        String location = "Kazan";

        LocalDateTime currentDate = LocalDateTime.parse(String.valueOf(LocalDateTime.now()));

        String weather = getWeather(location);

        String attachmentFilePath = "D:\\Java Projects\\WeatherAPI\\images.jpeg";

        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messageText +"\n\nLocation: " + location + "\nWeather: " + weather+"\nCurrent date : "+currentDate);

            multipart.addBodyPart(messageBodyPart);

            if (attachmentFilePath != null) {
                File attachment = new File(attachmentFilePath);
                if (attachment.exists()) {
                    BodyPart attachPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachment);
                    attachPart.setDataHandler(new DataHandler(source));
                    attachPart.setFileName(attachment.getName());
                    multipart.addBodyPart(attachPart);
                }
            }

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("Failed to send email. Error: " + e.getMessage());
        }
    }

    private static String getWeather(String location) {
        return WeatherAPI.getWeatherInfo(location);
    }
}
