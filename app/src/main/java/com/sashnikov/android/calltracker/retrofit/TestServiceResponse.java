package com.sashnikov.android.calltracker.retrofit;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Ilya_Sashnikau
 */
public class TestServiceResponse {

    @JsonProperty
    private Map<String, String> args;

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}
