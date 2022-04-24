package com.example.myapplication.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.screens.AddNewContact;
import com.google.android.material.tabs.TabLayout;

import java.util.PriorityQueue;
import java.util.Queue;

public class home extends AppCompatActivity {

    private ActivityMainBinding binding ;
    static final int PICK_CONTACT=1;
    private boolean clicked = false;
    private  String selectedContactNumber;
    private Animation rotOpen;
    private Animation rotClose;
    private Animation toBottom;
    private Animation fromBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        //Binding and drawing layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Tab layout setup
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        //Animation initialization
        rotOpen = AnimationUtils.loadAnimation(this , R.anim.rotate_open_anim);
        rotClose = AnimationUtils.loadAnimation(this , R.anim.rotate_close_anim);
        toBottom = AnimationUtils.loadAnimation(this , R.anim.to_bottom_anim);
        fromBottom = AnimationUtils.loadAnimation(this , R.anim.from_bottom_anim);


        //Adding Existing user
        binding.addExisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContacts();
            }
        });
        //Adding new user
        binding.addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewContact();
            }
        });
        //FAB of addition
        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked();
            }
        });
    }


    //waiting for result from contacts
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    getSelectedPhoneNumber(data);
                }
                break;
        }
    }

    //gets the contact from contacts cursor
    private void getSelectedPhoneNumber(Intent data) {
        Uri contactData = data.getData();
        Cursor c =  managedQuery(contactData, null, null, null, null);
        if (c.moveToFirst()) {
            String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String hasPhone =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (hasPhone.equalsIgnoreCase("1")) {
                Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                        null, null);
                phones.moveToFirst();
                selectedContactNumber= phones.getString(phones.getColumnIndexOrThrow("data1"));
                System.out.println(processPhone(selectedContactNumber));
            }
        }
    }

    private String processPhone(String selectedContactNumber) {
        Queue<Character> phone =  new PriorityQueue<>();
        StringBuilder str = new StringBuilder();

        for (int i = 0 ; i < selectedContactNumber.length();i++){
            if(selectedContactNumber.charAt(i) != ' ' ||selectedContactNumber.charAt(i) != '+' )
                phone.add(selectedContactNumber.charAt(i));
            str.append(phone.peek());
            phone.poll();
        }
        return str.toString();

    }

    //grants permission and opens contacts app
    private void openContacts() {
        if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(home.this, new String[] { Manifest.permission.READ_CONTACTS }, PICK_CONTACT);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }
    //grants permission and opens new contact activity
    private void addNewContact() {
        if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(home.this, new String[] { Manifest.permission.WRITE_CONTACTS }, PICK_CONTACT);
        }
        else
            AddNewContact.start(home.this);
    }

    //floating button functionalities
    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }
    //animation setters
    private void setAnimation( boolean clicked) {
        if(!clicked) {
            binding.addExisting.startAnimation(fromBottom);
            binding.addNew.startAnimation(fromBottom);
            binding.addFab.startAnimation(rotOpen);
        }else{
            binding.addExisting.startAnimation(toBottom);
            binding.addNew.startAnimation(toBottom);
            binding.addFab.startAnimation(rotClose);

        }
    }
    //visibility setters
    private void setVisibility( boolean clicked) {
        if(!clicked){
            binding.addExisting.setVisibility(View.VISIBLE);
            binding.addNew.setVisibility(View.VISIBLE);
        }else {
            binding.addExisting.setVisibility(View.INVISIBLE);
            binding.addNew.setVisibility(View.INVISIBLE);
        }
    }
    //clickability setter
    private void setClickable(boolean clicked){
        if (!clicked){
            binding.addExisting.setClickable(true);
            binding.addNew.setClickable(true);
        }else {
            binding.addExisting.setClickable(false);
            binding.addNew.setClickable(false);

        }

    }


}