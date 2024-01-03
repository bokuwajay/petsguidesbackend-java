package live.codeland.petsguidesbackend.auth;

import live.codeland.petsguidesbackend.config.JwtService;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User user) {

        String encodedPassword = passwordEncoder.encode((user.getPassword()));
        user.setPassword(encodedPassword);
       var res = userRepository.save(user);
        System.out.println("encoded Password------" + user.getPassword());
        System.out.println("Saved user response---------" + res);
        var jwtToken = jwtService.generateToken(res);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(User user) {
        System.out.println("authenticate email-----" + user.getEmail());
        System.out.println("authenticate password-----" + user.getPassword());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );
        var foundUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow();

        System.out.println("authenticate Service-----" + foundUser);
        var jwtToken = jwtService.generateToken(foundUser);
        return new AuthenticationResponse(jwtToken);
    }
}
