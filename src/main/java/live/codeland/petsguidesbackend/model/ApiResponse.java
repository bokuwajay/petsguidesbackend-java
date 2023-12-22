package live.codeland.petsguidesbackend.model;

import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

public class ApiResponse<T> {
    private HttpStatus status;

    private Integer statusCode;
    private T data;
    private String detail;
    private LocalDateTime timestamp;

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