package live.codeland.petsguidesbackend.phoneVerification;

import live.codeland.petsguidesbackend.config.jwt.JwtService;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.repository.UserRepository;
import live.codeland.petsguidesbackend.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

import java.util.Optional;

@Service
public class PhoneVerificationService {

    @Value("${twilio.service.id}")
    private String twilioServiceId;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private UserService userService;

    PhoneVerificationService(JwtService jwtService, UserRepository userRepository, UserService userService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public String phoneVerificationCodeDelivery(String jwt) {
        final String userEmail = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            final String userPhoneNumber = user.get().getPhone();
            Verification.creator(twilioServiceId, userPhoneNumber, "sms").create();
            return jwt;
        }
        return null;
    }

    public String phoneVerificationCodeConfirmation(String jwt, String userInputCode) {
        final String userEmail = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            final String userPhoneNumber = user.get().getPhone();
            String userId = user.get().getId();
            VerificationCheck verificationCheck = VerificationCheck.creator(twilioServiceId).setTo(userPhoneNumber)
                    .setCode(userInputCode).create();
            if (verificationCheck.getStatus().equals("approved")) {
                user.get().setPhoneVerified(true);
                User updatedUser = userService.updateUser(userId, user.get());
                return jwtService.generateToken(updatedUser);
            }
        }
        return null;
    }
}
