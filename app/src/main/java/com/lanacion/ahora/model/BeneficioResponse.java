package com.lanacion.ahora.model;

import com.lanacion.ahora.util.Utils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ignacio Saslavsky on 10/04/15.
 * correonano@gmail.com
 */
public class BeneficioResponse implements Serializable {

    public String id;
    public String _id;
    public Double[] point;
    public Imagenes imagenes;
    public String hasta;
    public String desde;
    public Establecimiento establecimiento;
    public Beneficio beneficio;

    public Date getHasta() {
        return Utils.parseDate(hasta);
    }

    public Date getDesde() {
        return Utils.parseDate(desde);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeneficioResponse that = (BeneficioResponse) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
