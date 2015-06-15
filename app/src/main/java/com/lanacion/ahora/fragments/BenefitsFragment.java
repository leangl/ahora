package com.lanacion.ahora.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lanacion.ahora.R;
import com.lanacion.ahora.activities.MainActivity;
import com.lanacion.ahora.api.AhoraService;
import com.lanacion.ahora.model.BeneficioResponse;
import com.lanacion.ahora.model.MyLocation;
import com.lanacion.ahora.util.DataStore;
import com.lanacion.ahora.util.Formatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import roboguice.inject.InjectView;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Ignacio Saslavsky on 10/04/15.
 * correonano@gmail.com
 */

public class BenefitsFragment extends BaseFragment {

    private static final String TAG = "BenefitsFragment";
    private static final String ARG_SEARCH = "ARG_SEARCH";

    @InjectView(R.id.benefits_list)
    private ListView mList;
    @InjectView(R.id.empty_list)
    private View mEmptyView;
    @InjectView(R.id.salir_button)
    private View mSalir;
    @InjectView(R.id.ver_button)
    private View mGuardados;
    @InjectView(R.id.info_bar)
    private TextView info;

    private ListAdapter mAdapter;

    private Boolean isSearch = false;

    public static BenefitsFragment newInstance(Boolean isSearch) {
        BenefitsFragment fragment = new BenefitsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SEARCH, isSearch);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSearch = getArguments().getBoolean(ARG_SEARCH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_benefits, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isSearch) {
            performSearch();
        } else {
            populateList(((MainActivity) getActivity()).getBeneficios());
            if(((MainActivity) getActivity()).getBarrioSelected() != null) {
                info.setText(((MainActivity) getActivity()).getBarrioSelected().Nombre);
            }
            mGuardados.setVisibility(View.GONE);
            mSalir.setVisibility(View.GONE);
        }
    }

    private void populateList(List<BeneficioResponse> list) {
        mAdapter = new ListAdapter(list);
        mList.setAdapter(mAdapter);
        mList.setEmptyView(mEmptyView);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).setBeneficioSelected(mAdapter.getItem(position));
                start(DetalleBeneficioFragment.newInstance());
            }
        });
    }

    public void performSearch() {
        showLoading();
        try {
            MyLocation lastLoc = DataStore.getInstance().getObject("last_location", MyLocation.class);
            AhoraService.getInstance().getBeneficios(lastLoc.lat, lastLoc.lon, 1000).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
                stopLoading();
                Log.d(TAG, String.valueOf(result.size()));
                populateList(result);
            }, error -> {
                // ERROR
                stopLoading();
                populateList(Collections.emptyList());
                Log.d("error", "error");
                Toast.makeText(getActivity(), getString(R.string.error_loading), Toast.LENGTH_LONG).show();
            });
        } catch (DataStore.ObjectNotFoundException e) {
            stopLoading();
            populateList(Collections.emptyList());
            Toast.makeText(getActivity(), "Por favor active la localizacion e intente nuevamente", Toast.LENGTH_LONG).show();
        }

        mSalir.setOnClickListener(v -> {
            start(SalidaFragment.newInstance());
        });

        mGuardados.setOnClickListener(v -> {
            Set<BeneficioResponse> guardados = DataStore.getInstance().getAll(BeneficioResponse.class);
            List<BeneficioResponse> list = new ArrayList<BeneficioResponse>(guardados);
            populateList(list);
            info.setText(getString(R.string.guardados));
        });
    }

    private class ListAdapter extends BaseAdapter {

        private List<BeneficioResponse> mBenefits;

        private ListAdapter(List<BeneficioResponse> benefits) {
            mBenefits = benefits;
        }

        @Override
        public int getCount() {
            return mBenefits.size();
        }

        @Override
        public BeneficioResponse getItem(int position) {
            return mBenefits.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView == null) {
                v = LayoutInflater.from(getActivity()).inflate(R.layout.benefit_item, parent, false);
                v.setTag(new ViewHolder(v));
            } else {
                v = convertView;
            }
            BeneficioResponse benefit = getItem(position);

            ViewHolder holder = (ViewHolder) v.getTag();


            if(benefit.imagenes != null && benefit.imagenes.pics != null) {
                String img = benefit.imagenes.pics[benefit.imagenes.pics.length - 1];
                Picasso.with(getActivity()).load(img).into(holder.image);
            }
            holder.title.setText(benefit.beneficio.nombre);
            holder.percentage.setText(benefit.beneficio.tipo);
            holder.fechaHasta.setText(getString(R.string.fecha_hasta, Formatter.formatDate(benefit.getHasta())));
            return v;
        }

        private class ViewHolder {

            private ImageView image;
            private TextView title;
            private TextView percentage;
            private TextView fechaHasta;

            private ViewHolder(View v) {
                image = (ImageView) v.findViewById(R.id.image_benefit);
                percentage = (TextView) v.findViewById(R.id.percentage);
                title = (TextView) v.findViewById(R.id.title_benefit);
                fechaHasta = (TextView) v.findViewById(R.id.fecha_hasta);
            }
        }

    }
}
