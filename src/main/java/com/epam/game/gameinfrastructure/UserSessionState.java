package com.epam.game.gameinfrastructure;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/18/2019
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSessionState {
    private final CloseStatus closeStatus;
    private final String errorMessage;
    private final boolean valid;

    public static UserSessionState valid() {
        return new UserSessionState(null, null, true);
    }

    public static UserSessionState invalid(CloseStatus closeStatus, String errorMessage) {
        return new UserSessionState(closeStatus, errorMessage, false);
    }
}
