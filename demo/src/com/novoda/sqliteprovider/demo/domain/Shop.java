package com.novoda.sqliteprovider.demo.domain;

import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable {

    private final String name;
    private final String postcode;
    private final List<Firework> fireworks;

    public Shop(String name, String postcode, List<Firework> fireworks) {
        this.name = name;
        this.postcode = postcode;
        this.fireworks = fireworks;
    }

    public String getName() {
        return name;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<Firework> getFireworks() {
        return fireworks;
    }
}
