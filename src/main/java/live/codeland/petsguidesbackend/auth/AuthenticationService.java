package live.codeland.petsguidesbackend.auth;

import live.codeland.petsguidesbackend.config.JwtService;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
       User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        User foundUser = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(foundUser);
        return new AuthenticationResponse(jwtToken);
    }
}
