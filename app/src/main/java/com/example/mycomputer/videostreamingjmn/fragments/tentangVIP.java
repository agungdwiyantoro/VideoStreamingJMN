package com.example.mycomputer.videostreamingjmn.fragments;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mycomputer.videostreamingjmn.R;

/**
 * Created by My Computer on 10/15/2016.
 */

//--- Fragment untuk list menu drawer tentangVIP

public class tentangVIP extends Fragment implements View.OnClickListener{

    ImageView back,goAbout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.aboutvip, container, false);

        back = (ImageView) v.findViewById(R.id.back);
        goAbout = (ImageView) v.findViewById(R.id.goAboutVIP);

        goAbout.setOnClickListener(this);
        back.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;

            case R.id.goAboutVIP:
                Intent toJMN = new Intent(Intent.ACTION_VIEW, Uri.parse("http://web.jogjamedianet.com/"));
                startActivity(toJMN);
                break;

        }
    }
}
