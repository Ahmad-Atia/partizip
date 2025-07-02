package com.partizip.gateway.interfaces;

import com.partizip.gateway.dto.ApiRequest;
import com.partizip.gateway.dto.AuthToken;

public interface SecurityHandler {
    AuthToken authenticate(ApiRequest request);
    boolean authorize(ApiRequest request);
}
