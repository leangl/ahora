package com.lanacion.ahora.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lanacion.ahora.Ahora;
import com.lanacion.ahora.model.Barrio;
import com.lanacion.ahora.model.BeneficioRequest;
import com.lanacion.ahora.model.BeneficioResponse;
import com.lanacion.ahora.model.Category;
import com.lanacion.ahora.model.SubCategory;
import com.lanacion.ahora.util.Constants;

import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;

/**
 * Created by Ignacio Saslavsky on 10/04/15.
 * correonano@gmail.com
 */
public class AhoraService {

    private static AhoraService sInstance;

    private AhoraApi mApi;

    private AhoraService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.ENDPOINT)
                .build();

        mApi = restAdapter.create(AhoraApi.class);
    }

    public static final synchronized AhoraService getInstance() {
        if (sInstance == null) {
            sInstance = new AhoraService();
        }
        return sInstance;
    }

    public Observable<List<BeneficioResponse>> getBeneficiosBySearch(Barrio barrio, int horas, List<String> keywords) {
        BeneficioRequest request = new BeneficioRequest();
        request.lat = barrio.Latitude;
        request.lon = barrio.Longitude;
        request.horas = horas;
        request.dis = 2000;

        StringBuilder sb = new StringBuilder();
        for (String key : keywords) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(key);
        }
        request.keywords = sb.toString();

        return mApi.getBeneficios(request);
    }

    public Observable<List<BeneficioResponse>> getBeneficios(double lat, double lon, int dis) {
        /*JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(Ahora.getInstance().getAssets().open("benefits.json")));
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
        return Observable.just(new Gson().fromJson(reader, new TypeToken<List<BeneficioResponse>>() {
        }.getType()));*/

        BeneficioRequest request = new BeneficioRequest();
        request.lat = lat;
        request.lon = lon;
        request.horas = 4;
        request.dis = dis;
        //request.keywords = "";
        request.keywords = keywords();

        return mApi.getBeneficios(request);
    }

    private String keywords() {
        List<Category> categories = Ahora.getInstance().getLikedCategories();
        StringBuilder keywords = new StringBuilder();
        for (Category cat : categories) {
            addKeyword(keywords, cat.name);
            for (SubCategory subcat : cat.subCategories) {
                if (subcat.liked) {
                    addKeyword(keywords, subcat.name);
                }
            }
        }
        return keywords.toString();
    }

    private void addKeyword(StringBuilder keywords, String keywork) {
        if (keywords.length() > 0) {
            keywords.append(",");
        }
        keywords.append(keywork);
    }

}
