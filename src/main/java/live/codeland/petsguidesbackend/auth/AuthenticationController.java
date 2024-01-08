package live.codeland.petsguidesbackend.auth;

import jakarta.validation.Valid;
import live.codeland.petsguidesbackend.model.ApiResponse;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.service.UserService;
import org.springframework.dao.DuplicateKeyException;
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
    private final UserService userService;

    public AuthenticationController(AuthenticationService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@Valid @RequestBody User user){
        try {
            AuthenticationResponse authResponse = authService.register(user);
            String message = "Successfully registered!";
            HttpStatus status = HttpStatus.OK;
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>(status, 200, authResponse, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());

        } catch (DuplicateKeyException exception){
            String message = "Catch in controller registration: " + exception.getMessage();
            HttpStatus status = HttpStatus.CONFLICT;
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>(status, 409, null, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        } catch (Exception e){
            String message = "Catch in controller registration: " + e.getMessage();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>(status, 500, null, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        }



    }


    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody User user){
        return ResponseEntity.ok(authService.authenticate(user));
    }

}
