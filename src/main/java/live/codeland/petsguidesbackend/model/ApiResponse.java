package live.codeland.petsguidesbackend.model;

import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

public class ApiResponse<T> {
    private final HttpStatus status;

    private final Integer statusCode;
    private final T data;
    private final String detail;
    private final LocalDateTime timestamp;

    public ApiResponse(HttpStatus status, Integer statusCode,T data, String detail, LocalDateTime timestamp) {
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
}