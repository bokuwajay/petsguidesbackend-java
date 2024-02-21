package live.codeland.petsguidesbackend.helpers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class EmailHelper {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public EmailHelper(){

    }

    public boolean sendVerificationEmail(String emailAddress, String templateName, Context context) {
        StringBuffer reason = null;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(emailAddress);
            helper.setSubject("Email Verification");
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException exception){
            System.out.println("Messaging Exception: " + exception.getMessage());
            return false;
        } catch (Exception ex){
            StringBuffer exception = new StringBuffer(ex.getMessage().toString());
            if (exception.indexOf("ConnectException") >= 0) {
                reason = new StringBuffer(" Unable to Connect Mail server");
                return false;
            } else if (exception.indexOf("SendFailedException") >= 0){
                reason = new StringBuffer("Wrong To Mail address");
                return false;
            } else if (exception.indexOf("FileNotFoundException") >= 0){
                reason = new StringBuffer("File Not Found at Specific location");
                return false;
            } else {
                reason = new StringBuffer("Email has not been sent.");
                return false;
            }
        }

    }

}
