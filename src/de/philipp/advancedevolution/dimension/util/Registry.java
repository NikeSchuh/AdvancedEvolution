package de.philipp.advancedevolution.dimension.util;

import java.util.ArrayList;
import java.util.Collection;

public class Registry<T> extends ArrayList<T> {

    public void register(T object) {
        add(object);
    }



}
