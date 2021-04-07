package com.example.mycomputer.videostreamingjmn.SlidingImage_Adapter;

/**
 * Created by My Computer on 10/21/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mycomputer.videostreamingjmn.R;
import com.example.mycomputer.videostreamingjmn.googlelogin;

import java.util.ArrayList;

//---- Adapter untuk imageslider iklan
public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<Bitmap> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    private int parameter;


    public SlidingImage_Adapter(Context context,ArrayList<Bitmap> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        parameter = position;
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.slide_image);

            imageView.setImageBitmap(IMAGES.get(position));
            imageView.invalidate();


        //---- setiap iklan diberi link URL
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBrowser = new Intent(Intent.ACTION_VIEW,Uri.parse(LoginClasses.SaveSharedPreference.getIklanLinkURL(context, googlelogin.IklanLink+parameter)));
                v.getContext().startActivity(toBrowser);
            }
        });
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}