package io.jsantoku.sensorservice.model.response;

import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ErrorResponse implements Serializable {

    private transient HttpStatus status;
    private Error error;

    public ErrorResponse() { }

    public ErrorResponse(@NotNull HttpStatus status, String message) {
        this();
        this.status = status;
        this.error = new Error(status, message);
    }

    public ErrorResponse(int code, String message) {
        this(resolveStatus(code), message);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Error getError() {
        return error;
    }

    private class Error implements Serializable {
        private int code;
        private String reason;
        private String message;

        public Error(@NotNull HttpStatus status, String message) {
            this.code = status.value();
            this.reason = status.getReasonPhrase();
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }

        public String getMessage() {
            return message;
        }
    }

    private static HttpStatus resolveStatus(int code) {
        HttpStatus status = HttpStatus.resolve(code);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return status;
    }
}
