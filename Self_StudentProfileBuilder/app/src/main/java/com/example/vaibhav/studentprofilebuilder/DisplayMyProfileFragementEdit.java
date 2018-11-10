package com.example.vaibhav.studentprofilebuilder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Struct;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayMyProfileFragementEdit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DisplayMyProfileFragementEdit extends Fragment {

    String firstName;
    String lastName;
    String studentId;
    String department;
    String imageId;
    StudentDetails mDescribable;
    static String image;

    private OnFragmentInteractionListener mListener;

    public DisplayMyProfileFragementEdit() {

    }


    @Override
    public void onResume() {
        super.onResume();
        mListener.setTitle(R.string.app_name_edit);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.display_my_profile_fragement_edit, container, false);

        if (getArguments() != null) {
             mDescribable = (StudentDetails) getArguments().getSerializable(
                    "params");
            firstName = mDescribable.firstName;
            lastName = mDescribable.lastName;
            studentId = mDescribable.studentId;
            department = mDescribable.department;
            imageId = mDescribable.avatarId;
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.setTitle(R.string.app_name_edit);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((TextView)getView().findViewById(R.id.nameText)).setText(firstName + " " +lastName);
        ((TextView)getView().findViewById(R.id.idText)).setText(studentId);
        ((TextView)getView().findViewById(R.id.departmentText)).setText(department);

        int index = mDescribable.avatarId.length()-1;
        ImageView avatar = ((ImageView) getView().findViewById(R.id.imageView_display));
        switch (Integer.parseInt(mDescribable.avatarId.substring(index,index+1)))
        {
            case 1:{avatar.setImageResource(R.drawable.avatar_f_1);image = "av1"; } ; break;
            case 2: {avatar.setImageResource(R.drawable.avatar_m_1);image = "av2"; }; break;
            case 3: {avatar.setImageResource(R.drawable.avatar_f_2);image = "av3"; }; break;
            case 4: {avatar.setImageResource(R.drawable.avatar_m_2);image = "av4"; }; break;
            case 5: {avatar.setImageResource(R.drawable.avatar_f_3);image = "av5"; }; break;
            case 6: {avatar.setImageResource(R.drawable.avatar_m_3);image = "av6"; }; break;
        }

        getView().findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.editMyProfile(mDescribable);
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void editMyProfile(StudentDetails studentDetails);
        void setTitle(int title);
    }
}
