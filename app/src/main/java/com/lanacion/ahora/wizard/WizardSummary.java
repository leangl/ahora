package com.lanacion.ahora.wizard;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lanacion.ahora.Ahora;
import com.lanacion.ahora.R;
import com.lanacion.ahora.model.Category;
import com.lanacion.ahora.model.SubCategory;

import java.util.List;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_wizard_summary)
public class WizardSummary extends RoboActionBarActivity {

    @InjectView(R.id.liked_categories)
    private TextView mLikedCategories;
    @InjectView(R.id.accept)
    private View mAccept;

    private List<Category> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategories = Ahora.getInstance().getLikedCategories();

        if (mCategories.isEmpty()) {
            mLikedCategories.setText("Estas siguiendo todas las categorías.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Category cat : mCategories) {
                if (sb.length() > 0) {
                    sb.append(" / ");
                }
                sb.append(cat.name);
                for (SubCategory subCat : cat.subCategories) {
                    if (subCat.liked) {
                        if (sb.length() > 0) {
                            sb.append(" / ");
                        }
                        sb.append(subCat.name);
                    }
                }
            }
            mLikedCategories.setText(sb);
        }
        mAccept.setOnClickListener(v -> {
            finish();
        });

    }


}
