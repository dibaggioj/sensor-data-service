package io.jsantoku.sensorservice.security;

import io.jsantoku.sensorservice.Constants;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class SensorBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(Constants.Headers.REALM);
        super.afterPropertiesSet();
    }
}