package com.lanacion.ahora;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;

import com.lanacion.ahora.model.Category;
import com.lanacion.ahora.model.SubCategory;
import com.lanacion.ahora.util.DataStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leandro on 10/04/2015.
 */
public class Ahora extends Application {

    private List<Category> mCategories;
    private static Ahora sInstance;
    private static Handler h = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mCategories = new ArrayList<>(DataStore.getInstance().getAll(Category.class));

        if (mCategories.isEmpty()) {
            buildCategories();
        }

        startService(new Intent(this, LocationService.class));
        startService(new Intent(this, PushReceiverService.class));
    }

    public static final void runOnUiThread(Runnable r) {
        h.post(r);
    }

    private void buildCategories() {
        mCategories = new ArrayList<Category>();
        mCategories.add(new Category("Gastronomía", R.drawable.gastronomia_bg,
                new SubCategory("Comida Rápida"),
                new SubCategory("Delivery"),
                new SubCategory("Parrilla"),
                new SubCategory("Gourmet"),
                new SubCategory("Casera"),
                new SubCategory("Internacional"),
                new SubCategory("Heladería"),
                new SubCategory("Desayunos"),
                new SubCategory("Bares"),
                new SubCategory("Pizza"),
                new SubCategory("Empanadas"),
                new SubCategory("Sushi"),
                new SubCategory("Pescados"),
                new SubCategory("Mediterránea"),
                new SubCategory("Española"),
                new SubCategory("Árabe"),
                new SubCategory("Peruana"),
                new SubCategory("Japonesa"),
                new SubCategory("Mexicana"),
                new SubCategory("Italiana")
        ));
        mCategories.add(new Category("Entretenimiento", R.drawable.movies2,
                new SubCategory("Cine"),
                new SubCategory("Teatro"),
                new SubCategory("Musicales"),
                new SubCategory("Recitales"),
                new SubCategory("Infantiles"),
                new SubCategory("Cultural"),
                new SubCategory("Aventura"),
                new SubCategory("Noche"),
                new SubCategory("Deportes"),
                new SubCategory("Eventos")
        ));
        mCategories.add(new Category("Moda", R.drawable.moda,
                new SubCategory("Indumentaria"),
                new SubCategory("Calzado"),
                new SubCategory("Infantiles"),
                new SubCategory("Accesorios")
        ));
        mCategories.add(new Category("Turismo", R.drawable.turismo2,
                new SubCategory("Hoteles"),
                new SubCategory("Paquetes"),
                new SubCategory("Pasajes"),
                new SubCategory("Servicios"),
                new SubCategory("Turísticos"),
                new SubCategory("Agencias")
        ));
        mCategories.add(new Category("Cuidado Personal", R.drawable.cuidado_personal,
                new SubCategory("Spa"),
                new SubCategory("Peluquería"),
                new SubCategory("Centros Estéticos"),
                new SubCategory("Gimnasio")
        ));
        mCategories.add(new Category("Otras", R.drawable.turismo3,
                new SubCategory("Auto"),
                new SubCategory("Hogar"),
                new SubCategory("Educación"),
                new SubCategory("Comercios")
        ));
        DataStore.getInstance().putAll(mCategories, Category.class);
    }

    public static final Ahora getInstance() {
        return sInstance;
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public List<Category> getLikedCategories() {
        List<Category> result = new ArrayList<>();
        for (Category cat : getCategories()) {
            if (cat.liked) result.add(cat);
        }
        return result;
    }
}
