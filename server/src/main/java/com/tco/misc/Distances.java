package com.tco.misc;

import java.util.ArrayList;

public class Distances extends ArrayList<Long> {

    public Distances() {
        super();
    }

    public Distances(ArrayList<Long> distances) {
        super(distances);
    }

    public long totalDistance() {
        return this.stream().mapToLong(Long::longValue).sum();
    }
}
