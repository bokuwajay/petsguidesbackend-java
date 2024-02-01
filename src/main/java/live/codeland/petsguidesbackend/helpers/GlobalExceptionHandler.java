package live.codeland.petsguidesbackend.helpers;

import live.codeland.petsguidesbackend.model.ApiResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad request (missing payload / data type incorrect)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {
        List<String> exceptionMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("%s: %s (Rejected value: %s)",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .collect(Collectors.toList());

        String exceptionMessage = String.join(", ", exceptionMessages);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<Object> exceptionResponse = new ApiResponse<>(status, status.value(), null, exceptionMessage, LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    // The auth pass the filter, but the password / email is wrong
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception){
        String exceptionMessage = exception.getMessage();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiResponse<Object> exceptionResponse = new ApiResponse<>(status, status.value(), null, exceptionMessage, LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }
    
    // 405 wrong http request method (GET/PATCH/POST/DELETE)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpException(HttpRequestMethodNotSupportedException exception){
        String exceptionMessage = exception.getBody().getDetail();
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ApiResponse<Object> exceptionResponse = new ApiResponse<>(status, status.value(), null,exceptionMessage, LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }


    // 409 data duplicated in DB
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException exception){
        final String exceptionMessage;
        Pattern pattern = Pattern.compile("dup key: \\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(exception.getMessage());
        if(matcher.find()){
            String matchedString = "Duplicate data {" + matcher.group(1) + "}";
            exceptionMessage = matchedString.replace("\"", "");
        } else {
            exceptionMessage = "Duplicate Exception: " + exception.getMessage();
        }
        HttpStatus status = HttpStatus.CONFLICT;
        ApiResponse<Object> exceptionResponse = new ApiResponse<>(status, status.value(), null, exceptionMessage, LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }


}
