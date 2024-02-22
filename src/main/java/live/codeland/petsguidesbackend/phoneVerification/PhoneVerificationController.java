package live.codeland.petsguidesbackend.phoneVerification;

import com.twilio.Twilio;
import com.twilio.exception.ApiConnectionException;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import live.codeland.petsguidesbackend.emailVerification.EmailVerificationResponse;
import live.codeland.petsguidesbackend.model.ApiResponse;
import live.codeland.petsguidesbackend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/verification/phone")
@Validated
public class PhoneVerificationController {

    private PhoneVerificationService phoneVerificationService;

    public PhoneVerificationController(PhoneVerificationService phoneVerificationService){
        this.phoneVerificationService = phoneVerificationService;
    }

    @PostMapping("/code-delivery")
    public ResponseEntity<ApiResponse<PhoneVerificationResponse>> phoneVerificationCodeDelivery(HttpServletRequest request){
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt = authHeader.substring(7);
            PhoneVerificationResponse phoneVerificationResponse = phoneVerificationService.phoneVerificationCodeDelivery(jwt);
            String message = "Successfully sent phone verification code!";
            HttpStatus status = HttpStatus.OK;
            ApiResponse<PhoneVerificationResponse> response = new ApiResponse<>(status, status.value(), phoneVerificationResponse, message, LocalDateTime.now());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        } catch (ApiException exception){
            String exceptionMessage = "Catch API Exception in phone verification code delivery: " + exception.getMessage();
            HttpStatus status = HttpStatus.resolve(exception.getStatusCode());
            ApiResponse<PhoneVerificationResponse> exceptionResponse = new ApiResponse<>(status, status.value(), null, exceptionMessage, LocalDateTime.now());
            return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
        } catch (ApiConnectionException exception){
            String exceptionMessage = "Catch Connection Exception in phone verification code delivery: " + exception.getMessage();
            HttpStatus status = HttpStatus.BAD_GATEWAY;
            ApiResponse<PhoneVerificationResponse> exceptionResponse = new ApiResponse<>(status, status.value(), null, exceptionMessage, LocalDateTime.now());
            return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
        } catch (Exception exception) {
            String exceptionMessage = "Catch run time exception in phone verification code delivery: " + exception.getMessage();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ApiResponse<PhoneVerificationResponse> exceptionResponse = new ApiResponse<>(status, status.value(), null, exceptionMessage, LocalDateTime.now());
            return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
        }
    }

}
