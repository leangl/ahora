package com.lanacion.ahora.api;

import com.lanacion.ahora.model.BeneficioRequest;
import com.lanacion.ahora.model.BeneficioResponse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

public interface AhoraApi {

    @POST("/benefits/locate")
    Observable<List<BeneficioResponse>> getBeneficios(@Body BeneficioRequest request);

}
