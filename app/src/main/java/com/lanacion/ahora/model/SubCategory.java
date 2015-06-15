package com.lanacion.ahora.model;

import java.io.Serializable;

public class SubCategory implements Serializable {

    public final String name;
    public boolean liked;

    public SubCategory(String name) {
        this.name = name;
    }
}