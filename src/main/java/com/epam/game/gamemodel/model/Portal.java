package com.epam.game.gamemodel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * @author Igor_Petrov@epam.com
 * Created at 2/4/2019
 */
@Data
@EqualsAndHashCode(callSuper = true, exclude = "ttl")
@ToString(callSuper = true)
public class Portal extends Edge {

    @JsonIgnore
    private int ttl;

    protected Portal(long source, long target, int ttl) {
        super(source, target);
        this.ttl = ttl;
    }

    public static Portal of(long source, long target, int ttl) {
        return new Portal(source, target, ttl);
    }

    public int countDownTtl() {
        return --ttl;
    }
}
