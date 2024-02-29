package live.codeland.petsguidesbackend.dto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;

public class ApiResponseDto<T> {
    private final HttpStatus status;

    private final Integer statusCode;
    private final T data;
    private final String detail;
    private final LocalDateTime timestamp;

    public ApiResponseDto(HttpStatus status, Integer statusCode, T data, String detail, LocalDateTime timestamp) {
        this.status = status;
        this.statusCode = statusCode;
        this.data = data;
        this.detail = detail;
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public T getData(){
        return data;
    }

    public String getDetail() {
        return detail;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ResponseEntity<ApiResponseDto<T>> toClient(){
        return new ResponseEntity<>(this, new HttpHeaders(), this.getStatus());
    }
}