package com.somethingsimple.yelpreviews.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.somethingsimple.yelpreviews.R;
import com.somethingsimple.yelpreviews.contracts.YelpDetailsServiceContract;
import com.somethingsimple.yelpreviews.models.Businesses;

import java.util.List;

public class DetailsViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageUrlList;
    private YelpDetailsServiceContract service;

    public DetailsViewPagerAdapter(final Context context, final List<String> imageUrlList, final YelpDetailsServiceContract service) {
        this.context = context;
        this.imageUrlList = imageUrlList;
        this.service = service;
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
        service.loadImage(imageView, imageUrlList.get(position));
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }
}
