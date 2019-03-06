package com.epam.game.gamemodel.model.disaster;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Igor_Petrov@epam.com
 * Created at 1/29/2019
 */
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"damageFactor"})
public class Disaster<T> {
    @Getter
    private DisasterType type;
    private T target;
    private int ttl;
    protected double damageFactor;

    public int calculateDamage(int unitsCount) {
        return (int) (unitsCount * damageFactor);
    }

    public int countDownTtl() {
        return --ttl;
    }

    public T getTarget() {
        return target;
    }
}
