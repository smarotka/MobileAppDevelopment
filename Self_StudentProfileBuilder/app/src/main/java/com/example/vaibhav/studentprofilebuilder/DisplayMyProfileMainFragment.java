package com.example.vaibhav.studentprofilebuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayMyProfileMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DisplayMyProfileMainFragment extends Fragment {
    public static String image;

    private OnFragmentInteractionListener mListener;

    String firstStudentName;
    String lastName;
    String studentID;
    String department;

    public DisplayMyProfileMainFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=  inflater.inflate(R.layout.fragment_display_my_profile_main, container, false);

        ((ImageView) view.findViewById(R.id.select_avatar_image_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(MyProfile_MainActivity.this,SelectAvatarActivity.class);
                startActivityForResult(intent,REQ_CODE);    */
                 }
        });

        ((Button) view.findViewById(R.id.save_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = true;
                EditText firstName = ((EditText) view.findViewById(R.id.first_name_editText));
                if(firstName.getText().toString().isEmpty() || firstName.getText().toString().equals("") )
                {
                    firstName.setError("Enter first Name");
                    ok = false;
                }
                if(((EditText) view.findViewById(R.id.last_name_editText)).getText().toString().isEmpty() || ((EditText) view.findViewById(R.id.last_name_editText)).getText().toString().equals("") )
                {
                    ((EditText) view.findViewById(R.id.last_name_editText)).setError("Enter Last Name");
                    ok = false;
                }
                if(((EditText) view.findViewById(R.id.student_id_editText)).getText().toString().isEmpty() || ((EditText) view.findViewById(R.id.student_id_editText)).getText().toString().equals("") )
                {

                    ((EditText) view.findViewById(R.id.student_id_editText)).setError("Enter Student Id");
                    ok = false;
                }

                else if(((EditText) view.findViewById(R.id.student_id_editText)).getText().toString().length() < 9){
                    ((EditText) view.findViewById(R.id.student_id_editText)).setError("Enter Student Id of 9 digits");
                    ok = false;}

                if(((RadioGroup) view.findViewById(R.id.dept_radio_group)).getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(view.getContext(),"Select Department",Toast.LENGTH_SHORT).show();
                    ok = false;
                }
                if(image == null || image.equals(""))
                {
                    Toast.makeText(view.getContext(),"Select Avatar",Toast.LENGTH_SHORT).show();
                    ok = false;
                }
                if(ok)
                {
                    /*Intent intent = new Intent(MyProfile_MainActivity.this,DisplayMyProfile.class);
                    int radioCheckedId = ((RadioGroup) view.findViewById(R.id.dept_radio_group)).getCheckedRadioButtonId();
                    StudentDetails studentDetails = new StudentDetails(

                    intent.putExtra(DisplayMyProfile.USER_KEY,studentDetails);

                    startActivityForResult(intent,USER_KEY_CODE);*/

                   firstStudentName = ((EditText) view.findViewById(R.id.first_name_editText)).getText().toString();

                   lastName = String.valueOf(((EditText) view.findViewById(R.id.last_name_editText)).getText());

                   studentID = String.valueOf(((EditText) view.findViewById(R.id.student_id_editText)).getText());
                    int radioCheckedId = ((RadioGroup) view.findViewById(R.id.dept_radio_group)).getCheckedRadioButtonId();

                    department =  ((RadioButton) view.findViewById(radioCheckedId)).getText().toString();
                    mListener.saveButtonClicked(firstStudentName,lastName,studentID,department,image );


                }

            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.setTitle(R.string.app_name_main);
        String selectedId = mListener.getSelectedAvatarId();
        setImageOnIDReceived(selectedId);

        /*if(getArguments() != null && getArguments().getSerializable("paramsEditBack") != null)
        {

            StudentDetails student = (StudentDetails) getArguments().getSerializable("paramsEditBack");
            getArguments().clear();
            setStudentDate(student);
        }*/
    }


    private void setImageOnIDReceived(String selectedId)
    {
        if(selectedId != null)
        {
            int index = selectedId.length()-1;
            ImageView avatar = ((ImageView) getView().findViewById(R.id.select_avatar_image_view));
            switch (Integer.parseInt(selectedId.substring(index,index+1)))
            {
                case 1:{avatar.setImageResource(R.drawable.avatar_f_1);image = "av1"; } ; break;
                case 2: {avatar.setImageResource(R.drawable.avatar_m_1);image = "av2"; }; break;
                case 3: {avatar.setImageResource(R.drawable.avatar_f_2);image = "av3"; }; break;
                case 4: {avatar.setImageResource(R.drawable.avatar_m_2);image = "av4"; }; break;
                case 5: {avatar.setImageResource(R.drawable.avatar_f_3);image = "av5"; }; break;
                case 6: {avatar.setImageResource(R.drawable.avatar_m_3);image = "av6"; }; break;
            }
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DisplayMyProfileMainFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.setTitle(R.string.app_name_main);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((ImageView) getView().findViewById(R.id.select_avatar_image_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToAvatar();

            }
        });

        if (getArguments() != null && (StudentDetails) getArguments().getSerializable("paramsEdit") != null) {
            StudentDetails student = (StudentDetails) getArguments().getSerializable("paramsEdit");
            getArguments().clear();
            setStudentDate(student);
        }
    }

    private void setStudentDate(StudentDetails student)
    {

        if(student.avatarId == null)
            ((ImageView) getView().findViewById(R.id.select_avatar_image_view)).setImageResource(R.drawable.select_image);
        else{
            String selectedId = student.avatarId;
            setImageOnIDReceived(selectedId);
        }
        ((EditText) getView().findViewById(R.id.first_name_editText)).setText(student.firstName);

        ((EditText) getView().findViewById(R.id.last_name_editText)).setText(student.lastName);

        ((EditText) getView().findViewById(R.id.student_id_editText)).setText(student.studentId);

        if(((RadioButton) getView().findViewById(R.id.bio_radioButton)).getText().equals(student.department))
            ((RadioButton) getView().findViewById(R.id.bio_radioButton)).setChecked(true);
        else
            ((RadioButton) getView().findViewById(R.id.bio_radioButton)).setChecked(false);

        if(((RadioButton) getView().findViewById(R.id.cs_radioButton)).getText().equals(student.department))
            ((RadioButton) getView().findViewById(R.id.cs_radioButton)).setChecked(true);
        else
            ((RadioButton) getView().findViewById(R.id.cs_radioButton)).setChecked(false);

        if(((RadioButton) getView().findViewById(R.id.sis_radioButton)).getText().equals(student.department))
            ((RadioButton) getView().findViewById(R.id.sis_radioButton)).setChecked(true);
        else
            ((RadioButton) getView().findViewById(R.id.sis_radioButton)).setChecked(false);

        if(((RadioButton) getView().findViewById(R.id.other_radioButton)).getText().equals(student.department))
            ((RadioButton) getView().findViewById(R.id.other_radioButton)).setChecked(true);
        else
            ((RadioButton) getView().findViewById(R.id.other_radioButton)).setChecked(false);

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
        public void saveButtonClicked(String fistName, String lastName, String studentId, String department, String image);

        void goToAvatar();
        String getSelectedAvatarId();
        void setTitle(int title);
    }
}
