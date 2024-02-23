package live.codeland.petsguidesbackend.emailVerification;

import live.codeland.petsguidesbackend.config.jwt.JwtService;
import live.codeland.petsguidesbackend.helpers.EmailHelper;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.repository.UserRepository;
import live.codeland.petsguidesbackend.service.UserService;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Optional;

@Service
public class EmailVerificationService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final EmailHelper emailHelper;

    private final UserService userService;

    EmailVerificationService(JwtService jwtService, UserRepository userRepository, EmailHelper emailHelper, UserService userService){
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.emailHelper = emailHelper;
        this.userService = userService;
    }

    public String emailVerificationCodeDelivery(String jwt){
        Context context = new Context();
        final String userEmail = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(userEmail);
        final String emailVerificationCode = user.get().getEmailVerificationCode();
        context.setVariable("verificationCode", emailVerificationCode);
        boolean successfullySent =  emailHelper.sendVerificationEmail(userEmail, "email-template", context);
        if(successfullySent){
            return jwtService.generateToken(user.get());
        }
        return null;
    }

    public String emailVerificationCodeConfirmation(String jwt, String userInputCode ){
        final String userEmail = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(userEmail);

        if(user.isPresent()){
            String userId = user.get().getId();
            final String dbVerificationCode = user.get().getEmailVerificationCode().toLowerCase();

            if(userInputCode.equals(dbVerificationCode)){
                user.get().setEmailVerified(true);
                User updatedUser = userService.updateUser(userId,user.get());
                return jwtService.generateToken(updatedUser);
            }
        }
        return null;
    }
}
