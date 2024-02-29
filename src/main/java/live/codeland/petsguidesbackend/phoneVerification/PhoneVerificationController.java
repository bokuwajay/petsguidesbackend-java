package live.codeland.petsguidesbackend.phoneVerification;

import com.twilio.exception.ApiConnectionException;
import com.twilio.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import live.codeland.petsguidesbackend.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<ApiResponseDto<Object>> phoneVerificationCodeDelivery(HttpServletRequest request){
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt = authHeader.substring(7);
            String jwtToken = phoneVerificationService.phoneVerificationCodeDelivery(jwt);
            ApiResponseDto<Object> response = new ApiResponseDto<>(HttpStatus.OK, 200, jwtToken, "Successfully sent phone verification code!", LocalDateTime.now());
            return response.toClient();
        } catch (ApiException exception){

            ApiResponseDto<Object> exceptionResponse = new ApiResponseDto<>(HttpStatus.resolve(exception.getStatusCode()), exception.getStatusCode(), null, "Catch API Exception in phone verification code delivery: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();

        } catch (ApiConnectionException exception){

            ApiResponseDto<Object> exceptionResponse = new ApiResponseDto<>(HttpStatus.BAD_GATEWAY, 502, null, "Catch Connection Exception in phone verification code delivery: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();

        } catch (Exception exception) {

            ApiResponseDto<Object> exceptionResponse = new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, 500, null, "Catch run time exception in phone verification code delivery: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();
        }
    }

    @PostMapping("/code-confirmation")
    public ResponseEntity<ApiResponseDto<Object>> phoneVerificationCodeConfirmation(@RequestParam String userInputCode, HttpServletRequest request){
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt = authHeader.substring(7);
            String jwtToken = phoneVerificationService.phoneVerificationCodeConfirmation(jwt, userInputCode);
            ApiResponseDto<Object> response = new ApiResponseDto<>(HttpStatus.OK, 200, jwtToken, "Successfully validate phone verification code!", LocalDateTime.now());
            return response.toClient();
        } catch (ApiException exception){

            ApiResponseDto<Object> exceptionResponse = new ApiResponseDto<>(HttpStatus.resolve(exception.getStatusCode()), exception.getStatusCode(), null, "Catch API Exception in phone verification code confirmation: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();

        } catch (ApiConnectionException exception){

            ApiResponseDto<Object> exceptionResponse = new ApiResponseDto<>(HttpStatus.BAD_GATEWAY, 502, null, "Catch Connection Exception in phone verification code confirmation: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();

        } catch (Exception exception) {

            ApiResponseDto<Object> exceptionResponse = new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, 500, null, "Catch run time exception in phone verification code confirmation: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();
        }

    }

}
