package com.lanacion.ahora.wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lanacion.ahora.Ahora;
import com.lanacion.ahora.R;
import com.lanacion.ahora.model.Category;
import com.lanacion.ahora.model.SubCategory;
import com.lanacion.ahora.util.DataStore;

import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_wizard)
public class WizardActivity extends RoboFragmentActivity {

    private List<Category> mCategories;
    private int mStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategories = Ahora.getInstance().getCategories();
        nextStep();
    }

    public void nextStep() {
        if (mStep == mCategories.size()) {
            saveAndFinish();
        } else {
            Category category = mCategories.get(mStep++);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, WizardStep.newInstance(category)).commit();
        }
    }

    public void skip() {
        for (Category cat : mCategories) {
            cat.liked = false;
            for (SubCategory subCat : cat.subCategories) {
                subCat.liked = false;
            }
        }
        saveAndFinish();
    }

    private void saveAndFinish() {
        DataStore.getInstance().putAll(mCategories, Category.class);
        DataStore.getInstance().putObject("wizard_completed", Boolean.TRUE);
        setResult(Activity.RESULT_OK);
        finish();
        startActivity(new Intent(this, WizardSummary.class));
    }

    public void like(Category category, List<SubCategory> subCategories) {
        nextStep();
    }

    public void dislike(Category category) {
        nextStep();
    }


}
