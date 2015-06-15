package com.lanacion.ahora.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lanacion.ahora.R;
import com.lanacion.ahora.model.Category;
import com.lanacion.ahora.model.SubCategory;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class WizardStep extends RoboFragment {

    @InjectView(R.id.category_name)
    private TextView mCategoryName;
    @InjectView(R.id.category_name_step2)
    private TextView mCategoryName2;
    @InjectView(R.id.like)
    private View mLike;
    @InjectView(R.id.dislike)
    private View mDislike;
    @InjectView(R.id.nextStep)
    private View mNextStep;
    @InjectView(R.id.skip)
    private View mSkip;
    @InjectView(R.id.subcategories)
    private ListView mSubcategories;
    @InjectView(R.id.subCategoriesStep)
    private ViewGroup subCategoriesStep;

    private Category mCategory;
    private SubCategoriesAdapter mAdapter;

    public static final WizardStep newInstance(Category category) {
        WizardStep instance = new WizardStep();
        instance.mCategory = category;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wizard_step, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getView().findViewById(R.id.background).setBackground(getResources().getDrawable(mCategory.imgId));

        mCategoryName.setText(mCategory.name);
        mCategoryName2.setText(mCategory.name);

        mAdapter = new SubCategoriesAdapter();
        mSubcategories.setAdapter(mAdapter);
        mSubcategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCategory.subCategories.get(i).liked = !mCategory.subCategories.get(i).liked;
                view.findViewById(R.id.checked).setVisibility(mCategory.subCategories.get(i).liked ? View.VISIBLE : View.INVISIBLE);
            }
        });
        mLike.setOnClickListener(v -> {
            mCategory.liked = true;
            subCategoriesStep.setVisibility(View.VISIBLE);
            view.animate().translationY(0);
        });
        mDislike.setOnClickListener(v -> {
            mCategory.liked = false;
            ((WizardActivity) getActivity()).dislike(mCategory);
        });
        mNextStep.setOnClickListener(v -> {
            ((WizardActivity) getActivity()).like(mCategory, mCategory.subCategories);
            ((WizardActivity) getActivity()).like(mCategory, mCategory.subCategories);
        });

        mSkip.setOnClickListener(v -> {
            ((WizardActivity) getActivity()).skip();
        });

    }

    private class SubCategoriesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCategory.subCategories.size();
        }

        @Override
        public SubCategory getItem(int i) {
            return mCategory.subCategories.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.subcategory, viewGroup, false);
            }

            SubCategory subCategory = getItem(i);

            TextView textView = (TextView) convertView.findViewById(R.id.subcategory_name);
            textView.setText(subCategory.name);
            convertView.findViewById(R.id.checked).setVisibility(subCategory.liked ? View.VISIBLE : View.INVISIBLE);
            return convertView;
        }
    }

}
