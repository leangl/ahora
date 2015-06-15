package com.lanacion.ahora.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Category implements Serializable {

    public final String name;
    public final int imgId;
    public final List<SubCategory> subCategories;
    public boolean liked;

    public Category(String name, int imgId, SubCategory... subCategories) {
        this.name = name;
        this.imgId = imgId;
        this.subCategories = Arrays.asList(subCategories);
    }

    public List<SubCategory> getLikedSubcategories() {
        if (!liked) return Collections.emptyList();

        List<SubCategory> result = new ArrayList<>();
        for (SubCategory sc : subCategories) {
            if (sc.liked) result.add(sc);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!name.equals(category.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

