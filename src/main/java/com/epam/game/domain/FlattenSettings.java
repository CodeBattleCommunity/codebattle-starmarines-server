package com.epam.game.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Igor_Petrov@epam.com
 * Created at 2/20/2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlattenSettings {
    private Map<String, String> opts;
    private Map<String, String> descriptions;
}
