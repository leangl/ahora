package com.lanacion.ahora.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lanacion.ahora.Ahora;
import com.lanacion.ahora.R;
import com.lanacion.ahora.activities.MainActivity;
import com.lanacion.ahora.api.AhoraService;
import com.lanacion.ahora.model.Barrio;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Ignacio Saslavsky on 11/04/15.
 * correonano@gmail.com
 */
public class SalidaFragment extends BaseFragment {

    @InjectView(R.id.aceptar_button)
    private View mAceptar;
    @InjectView(R.id.barrios_spinner)
    private Spinner mBarrios;
    @InjectView(R.id.comida_spinner)
    private Spinner mComida;
    @InjectView(R.id.salida_spinner)
    private Spinner mSalida;
    @InjectView(R.id.horas_spinner)
    private Spinner mHoras;

    List<Barrio> barrios;

    private static Map<Integer, List<String>> sKeywords;

    static {
        sKeywords = new HashMap<>();
        sKeywords.put(0, Arrays.asList("Gourmet", "Sushi "));
        sKeywords.put(1, Arrays.asList("Parrilla", "Pizza", "Empanadas", "Rapida"));
        sKeywords.put(2, Arrays.asList("Árabe", "Japonesa", "Mexicana", "Sushi"));
        sKeywords.put(3, Arrays.asList("Spa", "Peluquería", "Centros Estéticos"));
    }

    public static SalidaFragment newInstance() {
        SalidaFragment fragment = new SalidaFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_salida, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barrios = getBarrios();

        String[] barriosName = new String[barrios.size()];
        for (int i = 0; i < barrios.size(); i++) {
            barriosName[i] = barrios.get(i).Nombre;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, barriosName);
        mBarrios.setAdapter(adapter);

        mAceptar.setOnClickListener(v -> {
            performSearch();
        });
    }


    public void performSearch() {
        showLoading();
        Barrio selectedBarrio = barrios.get(mBarrios.getSelectedItemPosition());
        AhoraService.getInstance().getBeneficiosBySearch(selectedBarrio, mHoras.getSelectedItemPosition() + 1, getKeywords()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
            stopLoading();
            ((MainActivity)getActivity()).setBeneficios(result);
            ((MainActivity)getActivity()).setBarrioSelected(selectedBarrio);
            start(BenefitsFragment.newInstance(true));
        }, error -> {
            // ERROR
            stopLoading();
            Log.d("error", "error");
            Toast.makeText(getActivity(), getString(R.string.error_loading), Toast.LENGTH_LONG).show();
        });
    }

    public List<Barrio> getBarrios() {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(Ahora.getInstance().getAssets().open("barrios.json")));
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
        return new Gson().fromJson(reader, new TypeToken<List<Barrio>>() {
        }.getType());

    }

    private List<String> getKeywords() {
        List<String> keywords = new ArrayList<>(sKeywords.get(mComida.getSelectedItemPosition())); // make it mutable...
        keywords.add((String) mSalida.getSelectedItem());
        return keywords;
    }
}
