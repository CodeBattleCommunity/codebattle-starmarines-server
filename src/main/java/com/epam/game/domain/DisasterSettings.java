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

    private double localDisasterProbability;
    private double localDisasterFactor;
    private double localDisasterDamage;
    private int localDisasterTtl;
    private double interPlanetDisasterProbability;
    private double interPlanetDisasterFactor;
    private double interPlanetDisasterDamage;
    private int interPlanetDisasterTtl;
}
