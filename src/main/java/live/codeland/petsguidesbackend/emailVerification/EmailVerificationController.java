package live.codeland.petsguidesbackend.emailVerification;

import live.codeland.petsguidesbackend.model.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/verification/email")
@Validated
public class EmailVerificationController {


    private EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService){
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/code-delivery")
    public ResponseEntity<ApiResponse<EmailVerificationResponse>> emailVerificationCodeDelivery(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        final String jwt = authHeader.substring(7);
       EmailVerificationResponse emailVerificationResponse = emailVerificationService.emailVerificationCodeDelivery(jwt);
        String message = "Successfully sent email verification code!";
        HttpStatus status = HttpStatus.OK;
        ApiResponse<EmailVerificationResponse> response = new ApiResponse<>(status, status.value(), emailVerificationResponse, message, LocalDateTime.now());
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    @PatchMapping("/code-confirmation")
    public ResponseEntity<ApiResponse<EmailVerificationResponse>> emailVerificationCodeConfirmation(HttpServletRequest request){
        final String userInputCode = request.getParameter("userInputCode").toLowerCase();
        final String authHeader = request.getHeader("Authorization");
        final String jwt = authHeader.substring(7);
        EmailVerificationResponse emailVerificationResponse = emailVerificationService.emailVerificationCodeConfirmation(jwt, userInputCode);
        String message = "Successfully validate email verification code!";
        HttpStatus status = HttpStatus.OK;
        ApiResponse<EmailVerificationResponse> response = new ApiResponse<>(status, status.value(), emailVerificationResponse, message, LocalDateTime.now());
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

}
