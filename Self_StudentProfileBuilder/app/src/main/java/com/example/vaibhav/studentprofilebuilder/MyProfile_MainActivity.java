
package com.example.vaibhav.studentprofilebuilder;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyProfile_MainActivity extends AppCompatActivity implements DisplayMyProfileMainFragment.OnFragmentInteractionListener, DisplayMyProfileFragementEdit.OnFragmentInteractionListener, Avatar_Fragment.OnFragmentInteractionListener {

    private String AVATAR_TAG = "AVATAR";
    private String ADD_STUDENT_TAG = "ADD_STUDENT";
    private String SELECTED_IMAGE = null;
    private DisplayMyProfileMainFragment displayMyProfileMainFragment= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile__main);
        displayMyProfileMainFragment = new DisplayMyProfileMainFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.containerDisplayProfile,displayMyProfileMainFragment, "DisplayProfile")
                .commit();

    }


    @Override
    public void saveButtonClicked(String fistName, String lastName, String studentId, String department, String image) {

        StudentDetails studentDetails = new StudentDetails(fistName,lastName,studentId,department, image);

        Bundle bundle = new Bundle();

        bundle.putSerializable("params", studentDetails);


        DisplayMyProfileFragementEdit myObj = new DisplayMyProfileFragementEdit();
        myObj.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.containerDisplayProfile, myObj, "EditDisplay")
                .addToBackStack("EditDisplay")
                .commit();
    }

    @Override
    public void editMyProfile(StudentDetails studentDetails) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("paramsEdit", studentDetails);
        displayMyProfileMainFragment.setArguments(bundle);


        if(getFragmentManager().getBackStackEntryCount()>0)
        {
            getFragmentManager().popBackStack();
        }

        /*getFragmentManager().beginTransaction()
                .add(R.id.containerDisplayProfile, myObj, "EditDisplay")
                .commit();*/
    }



    @Override
    public void goToAvatar() {
        getFragmentManager().beginTransaction()
                .replace(R.id.containerDisplayProfile, new Avatar_Fragment(),AVATAR_TAG)
                .addToBackStack(AVATAR_TAG)
                .commit();
    }

    public void sendAvatarId(String id)
    {
        SELECTED_IMAGE = id;

        if(getFragmentManager().getBackStackEntryCount()>0)
        {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void setTitle(int title) {
        getSupportActionBar().setTitle(title);
    }

    public String getSelectedAvatarId()
    {
        return  SELECTED_IMAGE;
    }
    @Override
    public void onBackPressed() {

       /*if(getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount()-1).getName().equalsIgnoreCase("EditDisplay"))
       {
           Bundle bundle = new Bundle();
           bundle.putSerializable("paramsEditBack", new StudentDetails(null,null,null,null,null));
           displayMyProfileMainFragment.setArguments(bundle);
       }*/
        if(getFragmentManager().getBackStackEntryCount()>0)
        {
            getFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }
    }
}