package com.mardonaquiz.mardona;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Button Create_Quiz = (Button)rootView.findViewById(R.id.button4);
        Create_Quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Quiz = new Intent(getActivity().getApplication(), CreateQuizActivity.class);
                startActivity(Quiz);
            }
        });







        return rootView;
    }

}