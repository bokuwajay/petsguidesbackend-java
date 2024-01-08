package live.codeland.petsguidesbackend.helpers;

import live.codeland.petsguidesbackend.auth.AuthenticationResponse;
import live.codeland.petsguidesbackend.model.ApiResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad request (missing payload / data type incorrect)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException validationException) {
        List<String> errorMessages = validationException.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("%s: %s (Rejected value: %s)",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .collect(Collectors.toList());

        String errorMessage = String.join(", ", errorMessages);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse errorResponse = new ApiResponse(status, status.value(), null, errorMessage, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    // 401 Unauthorized (invalid token/ missing token / expired token / anything related to authentication)
    @ExceptionHandler(CustomJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<Object>> handleJwtException(CustomJwtException customJwtException){
        String errorMessage = customJwtException.getMessage();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        if(customJwtException.isExpired()){
            errorMessage = "JWT token is expired";
            status = HttpStatus.UNAUTHORIZED;
        }

        ApiResponse<Object> customError = new ApiResponse(status, status.value(), null,errorMessage, LocalDateTime.now());
        return new ResponseEntity<>(customError, new HttpHeaders(), customError.getStatus());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException duplicateKeyException){
        final String message;
        Pattern pattern = Pattern.compile("dup key: \\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(duplicateKeyException.getMessage());
        if(matcher.find()){
            message = "Duplicate data {" + matcher.group(1) + "}";
        } else {
            message = "Duplicate Exception: " + duplicateKeyException.getMessage();
        }
        HttpStatus status = HttpStatus.CONFLICT;
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>(status, status.value(), null, message, LocalDateTime.now());
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpException(HttpRequestMethodNotSupportedException exception){
        String errorMessage = exception.getBody().getDetail();
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ApiResponse customError = new ApiResponse(status, status.value(), null,errorMessage, LocalDateTime.now());
        return new ResponseEntity<>(customError, new HttpHeaders(), customError.getStatus());
    }
}
