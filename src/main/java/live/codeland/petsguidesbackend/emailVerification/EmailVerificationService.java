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

    private JwtService jwtService;

    private UserRepository userRepository;

    private EmailHelper emailHelper;

    private UserService userService;

    EmailVerificationService(JwtService jwtService, UserRepository userRepository, EmailHelper emailHelper, UserService userService){
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.emailHelper = emailHelper;
        this.userService = userService;
    }

    public EmailVerificationResponse emailVerificationCodeDelivery(String jwt){
        Context context = new Context();
        final String userEmail = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(userEmail);
        final String emailVerificationCode = user.get().getEmailVerificationCode();
        context.setVariable("verificationCode", emailVerificationCode);
      boolean successfullySent =  emailHelper.sendVerificationEmail(userEmail, "email-template", context);
      if(successfullySent){
          String jwtToken = jwtService.generateToken(user.get());
          return new EmailVerificationResponse(jwtToken);
      } else {
          return new EmailVerificationResponse(null);
      }

    }

    public EmailVerificationResponse emailVerificationCodeConfirmation(String jwt, String userInputCode ){
        final String userEmail = jwtService.extractUsername(jwt);
        Optional<User> user = userRepository.findByEmail(userEmail);
        String userId = user.get().getId();
        final String dbVerificationCode = user.get().getEmailVerificationCode().toLowerCase();

        if(userInputCode.equals(dbVerificationCode)){
            user.get().setEmailVerified(true);
          User updatedUser = userService.updateUser(userId,user.get());
            String jwtToken = jwtService.generateToken(updatedUser);
            return new EmailVerificationResponse(jwtToken);
        }
        return null;
    }
}
