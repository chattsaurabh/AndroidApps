package com.somethingsimple.yelpreviews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.somethingsimple.yelpreviews.R;
import com.somethingsimple.yelpreviews.models.Businesses;
import com.somethingsimple.yelpreviews.presenters.YelpReviewPresenter;

public class GridAdapter extends BaseAdapter {
    private YelpReviewPresenter presenter;
    private Context context;

    public GridAdapter(YelpReviewPresenter presenter, final Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public int getCount() {
        return presenter.getBusinessList().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            Businesses businesses = presenter.getBusinessList().get(position);
            grid = inflater.inflate(R.layout.grid_item, null);
            ImageView image = grid.findViewById(R.id.gridItemImageView);
            TextView name = grid.findViewById(R.id.itemNameTxtView);
            TextView alias = grid.findViewById(R.id.itemAliasTxtView);
            presenter.loadImage(image, businesses.image_url);
            name.setText(businesses.name);
            alias.setText(businesses.alias);
            grid.setOnClickListener(presenter.getItemClickListenter(businesses));


        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
