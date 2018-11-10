package com.example.vaibhav.studentprofilebuilder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Avatar_Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Avatar_Fragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.setTitle(R.string.app_name_av);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.setTitle(R.string.app_name_av);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avatar_, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.avatar1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.sendAvatarId(getView().getResources().getResourceName(R.id.avatar1));
            }
        });

        getView().findViewById(R.id.avatar2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.sendAvatarId(getView().getResources().getResourceName(R.id.avatar2));
            }
        });

        getView().findViewById(R.id.avatar3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.sendAvatarId(getView().getResources().getResourceName(R.id.avatar3));

            }
        });
        getView().findViewById(R.id.avatar4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.sendAvatarId(getView().getResources().getResourceName(R.id.avatar4));
            }
        });
        getView().findViewById(R.id.avatar5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.sendAvatarId(getView().getResources().getResourceName(R.id.avatar5));
            }
        });
        getView().findViewById(R.id.avatar6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.sendAvatarId(getView().getResources().getResourceName(R.id.avatar6));
            }
        });
    }


    public interface OnFragmentInteractionListener {
        void sendAvatarId(String id);
        void setTitle(int title);
    }

}
