package live.codeland.petsguidesbackend.auth;

import jakarta.validation.Valid;
import live.codeland.petsguidesbackend.model.ApiResponse;
import live.codeland.petsguidesbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthenticationController {

    private final AuthenticationService authService;


    @Autowired
    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@Valid @RequestBody User user){
            AuthenticationResponse authResponse = authService.register(user);
            String message = "Successfully registered!";
            HttpStatus status = HttpStatus.OK;
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>(status, status.value(), authResponse, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }


    @PostMapping("/authentication")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@Valid @RequestBody AuthRequest authRequest ){

            AuthenticationResponse authResponse = authService.authenticate(authRequest);
            String message = "Successfully Authenticated!";
            HttpStatus status = HttpStatus.OK;
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>(status, status.value(), authResponse, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());

    }

}
