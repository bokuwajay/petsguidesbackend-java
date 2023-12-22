package live.codeland.petsguidesbackend.helpers;

import live.codeland.petsguidesbackend.model.ApiResponse;
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
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException validationException) {
        List<String> errorMessages = validationException.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("%s: %s (Rejected value: %s)",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .collect(Collectors.toList());

        String errorMessage = String.join(", ", errorMessages);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse errorResponse = new ApiResponse(status,400,null, errorMessage, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Object> handleHttpException(HttpRequestMethodNotSupportedException exception){
        String errorMessage = exception.getBody().getDetail();
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ApiResponse customError = new ApiResponse(status, 405,null,errorMessage, LocalDateTime.now());
        return new ResponseEntity<>(customError, new HttpHeaders(), customError.getStatus());
    }
}
