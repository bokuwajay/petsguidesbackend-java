package live.codeland.petsguidesbackend.auth;

import jakarta.validation.Valid;
import live.codeland.petsguidesbackend.helpers.dto.PaginationDto;
import live.codeland.petsguidesbackend.model.ApiResponse;
import live.codeland.petsguidesbackend.model.User;
import live.codeland.petsguidesbackend.service.UserService;
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
            AuthenticationResponse authResponse = authService.register(user);
            String message = "Successfully registered!";
            HttpStatus status = HttpStatus.OK;
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>(status, status.value(), authResponse, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }


    @PostMapping("/authentication")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@Valid @RequestBody AuthRequest authRequest){
        try {
            AuthenticationResponse authResponse = authService.authenticate(authRequest);
            String message = "Successfully Authenticated!";
            HttpStatus status = HttpStatus.OK;
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>(status, status.value(), authResponse, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        } catch (Exception exception){
            String exceptionMessage = "Catch in controller getAllUser: " + exception.getMessage();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ApiResponse<AuthenticationResponse> exceptionResponse = new ApiResponse<>(status, status.value(), null, exceptionMessage, LocalDateTime.now());
            return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
        }
    }

}
