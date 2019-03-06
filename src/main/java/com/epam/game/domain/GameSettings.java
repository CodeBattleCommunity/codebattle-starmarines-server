package com.epam.game.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameSettings {
    private long turnDelayMs;
    private List<String> trainigBotLogins;
    private LocalDateTime nextGame;
    private long clientTimeoutMs;
    private long roundTurns;
    private int minPlayers;
    private int maxPlayers;
    private boolean registrationOpened;
    private boolean gameCreationEnabled;
    private long errorDelayMs;
    private int startRowsToShow;
    private long playerActionsLimitPerCommand;
    private DisasterSettings disasterSettings;
    private PortalSettings portalSettings;
    private DocInfo docInfo;
}
