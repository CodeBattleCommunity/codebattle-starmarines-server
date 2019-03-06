package com.epam.game.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/30/2019
 */
@Data
@Builder
public class DocInfo {
    private String gameSourcesURL;
    private String gameClientsURL;
}
