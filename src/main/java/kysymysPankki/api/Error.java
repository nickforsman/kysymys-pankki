package kysymysPankki.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    private int code;
    private String message;

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @JsonProperty
    public int getCode() {
        return code;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }
}
