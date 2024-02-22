package live.codeland.petsguidesbackend.config.twilio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties("twilio")
public class TwilioConfiguration {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;


    public String getAccountSid() {
        return accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }


}
