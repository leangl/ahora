package com.lanacion.ahora.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lanacion.ahora.R;
import com.lanacion.ahora.activities.MainActivity;
import com.lanacion.ahora.model.BeneficioResponse;
import com.lanacion.ahora.util.DataStore;
import com.squareup.picasso.Picasso;

import roboguice.inject.InjectView;

/**
 * Created by Ignacio Saslavsky on 11/04/15.
 * correonano@gmail.com
 */
public class DetalleBeneficioFragment extends BaseFragment {

    @InjectView(R.id.name)
    private TextView nombre;
    @InjectView(R.id.direccion)
    private TextView direccion;
    @InjectView(R.id.percentage_detail)
    private TextView percentage_detail;
    @InjectView(R.id.descripcion)
    private TextView descripcion;
    @InjectView(R.id.map)
    private ImageView map;
    @InjectView(R.id.street)
    private ImageView street;
    @InjectView(R.id.detail_image)
    private ImageView detailImage;
    @InjectView(R.id.guardar_button)
    private View guardar;

    public static DetalleBeneficioFragment newInstance() {
        DetalleBeneficioFragment fragment = new DetalleBeneficioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BeneficioResponse beneficio = ((MainActivity) getActivity()).getBeneficioSelected();
        nombre.setText(beneficio.establecimiento.nombre);
        direccion.setText(beneficio.establecimiento.direccion);
        descripcion.setText(beneficio.beneficio.descripcion);
        percentage_detail.setText(beneficio.beneficio.tipo);
        if(beneficio.imagenes != null && beneficio.imagenes.pics != null) {
            String img = beneficio.imagenes.pics[beneficio.imagenes.pics.length - 1];
            Picasso.with(getActivity()).load(img).into(detailImage);
            if(beneficio.imagenes.googleMaps != null) {
                Picasso.with(getActivity()).load(beneficio.imagenes.googleMaps).into(map);
            }
            if(beneficio.imagenes.streetView != null) {
                Picasso.with(getActivity()).load(beneficio.imagenes.streetView).into(street);
            }
        }
        guardar.setOnClickListener(v->{
            DataStore.getInstance().putObject(beneficio);
            Toast.makeText(getActivity(), getString(R.string.guardado), Toast.LENGTH_LONG).show();
        });
    }
}
