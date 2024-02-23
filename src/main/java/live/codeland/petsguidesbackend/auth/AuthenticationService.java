package live.codeland.petsguidesbackend.auth;

import live.codeland.petsguidesbackend.config.jwt.JwtService;
import live.codeland.petsguidesbackend.helpers.VerificationCodeGenerator;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;



    public String register(User user) {
        String encodedPassword = passwordEncoder.encode((user.getPassword()));
        user.setPassword(encodedPassword);
        user.setEmailVerificationCode(VerificationCodeGenerator.generateVerificationCode());
        User savedUser = userRepository.save(user);
        return jwtService.generateToken(savedUser);
    }

    public String authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        User foundUser = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow();

        return jwtService.generateToken(foundUser);
    }
}
