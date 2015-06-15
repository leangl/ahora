package com.lanacion.ahora.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lanacion.ahora.R;
import com.lanacion.ahora.fragments.BenefitsFragment;
import com.lanacion.ahora.fragments.DetalleBeneficioFragment;
import com.lanacion.ahora.model.Barrio;
import com.lanacion.ahora.model.BeneficioResponse;
import com.lanacion.ahora.util.DataStore;
import com.lanacion.ahora.wizard.WizardActivity;

import java.util.List;

import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    public static final String BENEFICIO = "beneficio";

    List<BeneficioResponse> beneficios;
    BeneficioResponse beneficioSelected;
    Barrio barrioSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!DataStore.getInstance().contains("wizard_completed", Boolean.class)) {
            startActivityForResult(new Intent(this, WizardActivity.class), 1001);
        } else {
            start();
        }
    }

    private void start() {
        Intent i = getIntent();
        if (i.hasExtra(BENEFICIO)) {
            BeneficioResponse b = (BeneficioResponse) i.getSerializableExtra(BENEFICIO);
            setBeneficioSelected(b);
            start(DetalleBeneficioFragment.newInstance(), false);
        } else {
            start(BenefitsFragment.newInstance(false), false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                start();
            } else {
                finish();
            }
        }
    }

    public List<BeneficioResponse> getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(List<BeneficioResponse> beneficios) {
        this.beneficios = beneficios;
    }

    public BeneficioResponse getBeneficioSelected() {
        return beneficioSelected;
    }

    public void setBeneficioSelected(BeneficioResponse beneficioSelected) {
        this.beneficioSelected = beneficioSelected;
    }

    public Barrio getBarrioSelected() {
        return barrioSelected;
    }

    public void setBarrioSelected(Barrio barrioSelected) {
        this.barrioSelected = barrioSelected;
    }

    @Override
    protected void onNewIntent(Intent i) {
        super.onNewIntent(i);
        if (i.hasExtra(BENEFICIO)) {
            BeneficioResponse b = (BeneficioResponse) i.getSerializableExtra(BENEFICIO);
            setBeneficioSelected(b);
            start(DetalleBeneficioFragment.newInstance(), false);
        }
    }
}
