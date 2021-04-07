package com.example.mycomputer.videostreamingjmn.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mycomputer.videostreamingjmn.R;



/**
 * Created by My Computer on 5/2/2016.
 */

//--- Fragment untuk list menu drawer tentangJMN
public class tentangJMN extends Fragment implements View.OnClickListener{

    private ImageView tombolGo,back;
    private int indexRow = 0;

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about,container,false);
        tombolGo = (ImageView) v.findViewById(R.id.tombolgo);
        back = (ImageView) v.findViewById(R.id.back);

        tombolGo.setOnClickListener(this);
        back.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tombolgo:
                Intent toJMN = new Intent(Intent.ACTION_VIEW, Uri.parse("http://web.jogjamedianet.com/"));
                startActivity(toJMN);
                break;

            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }
}
