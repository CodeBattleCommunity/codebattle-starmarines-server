package com.epam.game.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/29/2019
 */
@Data
@AllArgsConstructor
@Builder
public class DisasterSettings {

    public static DisasterSettings DEFAULT = DisasterSettings.builder().build();

    private double localDisasterFactor;
    private double localDisasterDamage;
    private double interPlanetDisasterFactor;
    private double interPlanetDisasterDamage;
}
