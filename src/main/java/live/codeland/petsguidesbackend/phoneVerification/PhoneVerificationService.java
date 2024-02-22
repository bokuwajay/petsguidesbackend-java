package live.codeland.petsguidesbackend.phoneVerification;

import com.twilio.Twilio;
import com.twilio.exception.ApiConnectionException;
import com.twilio.exception.ApiException;
import jakarta.annotation.PostConstruct;
import live.codeland.petsguidesbackend.config.jwt.JwtService;
import live.codeland.petsguidesbackend.emailVerification.EmailVerificationResponse;
import live.codeland.petsguidesbackend.helpers.EmailHelper;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.repository.UserRepository;
import live.codeland.petsguidesbackend.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.rest.verify.v2.service.Verification;

import java.util.Optional;

@Service
public class PhoneVerificationService {

    @Value("${twilio.service.id}")
    private String twilioServiceId;


    private JwtService jwtService;

    private UserRepository userRepository;


    private UserService userService;

    PhoneVerificationService(JwtService jwtService, UserRepository userRepository, UserService userService){
        this.jwtService = jwtService;
        this.userRepository =userRepository;
        this.userService = userService;
    }

    public PhoneVerificationResponse phoneVerificationCodeDelivery(String jwt){
            final String userEmail = jwtService.extractUsername(jwt);
            Optional<User> user = userRepository.findByEmail(userEmail);
            final String userPhoneNumber = user.get().getPhone();
            Verification verification = Verification.creator(twilioServiceId, userPhoneNumber, "sms").create();
            return new PhoneVerificationResponse(jwt);
    }
}
