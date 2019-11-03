package io.jsantoku.sensorservice.model.response;

import java.io.Serializable;

public class CreateResponse implements Serializable {

    private String key;

    public CreateResponse() { }

    public CreateResponse(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

