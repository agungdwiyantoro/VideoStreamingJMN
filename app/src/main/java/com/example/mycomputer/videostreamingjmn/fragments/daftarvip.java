package com.example.mycomputer.videostreamingjmn.fragments;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.mycomputer.videostreamingjmn.R;

/**
 * Created by My Computer on 11/1/2016.
 */

//--- Fragment untuk pop up daftarVIP
public class daftarvip extends Fragment {

    private ImageView tombol_daftar_vip;
    private FrameLayout frameLayout;
    //@Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daftarvip,container,false);

        tombol_daftar_vip = (ImageView) view.findViewById(R.id.daftar_vip);
        frameLayout = (FrameLayout) view.findViewById(R.id.framelayout);

        tombol_daftar_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toJMN = new Intent(Intent.ACTION_VIEW, Uri.parse("http://web.jogjamedianet.com/"));
                startActivity(toJMN);
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        return view;
    }

}
