package com.example.shookisha.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shookisha.R;

public class AchatFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("AchatFragment :: onCreate()");


        /*TextView title =  getActivity().findViewById(R.id.title);
        getActivity().setTitle("Achat");
        title.setText(" ");*/
        ImageView logo = getActivity().findViewById(R.id.logoShookisha);
        logo.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // context = getActivity();
        View root = inflater.inflate(R.layout.fragment_achat, container, false);
        //TextView textView = root.findViewById(R.id.achatTtextView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("AchatFragment :: onStart()");

    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("AchatFragment :: onStop()");

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("AchatFragment :: onResume()");
    }
}
