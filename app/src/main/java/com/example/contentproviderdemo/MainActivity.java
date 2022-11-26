package com.example.contentproviderdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.contentproviderdemo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkPermissions();

        binding.button.setOnClickListener(view -> {
            createRecyclerView(getPhoneContacts());
        });
    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    0
            );
        }
    }

    private void createRecyclerView(List<Contact> contactList){
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(contactList);
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
        binding.linearLayout.addView(recyclerView);
    }

    private List<Contact> getPhoneContacts() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = contentResolver.query(
                uri,
                null,
                null,
                null,
                null
        );

        List<Contact> contactList = new ArrayList<>();
        int count = cursor.getCount();
        Log.i("CONTACT_PROVIDER_DEMO", "TOTAL # of Contacts: " + count);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactName = count > 0 ?
                        cursor.getString(
                                cursor.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                                )
                        ) : "empty";
                String contactNumber = count > 0 ?
                        cursor.getString(
                                cursor.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                        ) : "empty";

                Log.i("CONTACT_PROVIDER_DEMO", "Contact Name: " + contactName + ", Phone: " + contactNumber);
                contactList.add(new Contact(
                        contactName,
                        contactNumber
                ));
            }
        }
        return contactList;
    }
}