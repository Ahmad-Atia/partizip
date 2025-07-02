package com.partizip.gateway.interfaces;

import com.partizip.gateway.dto.AuthToken;
import java.util.UUID;

public interface TokenProvider {
    AuthToken generateToken(UUID userID);
    boolean validateToken(String token);
    UUID extractUserID(String token);
}
