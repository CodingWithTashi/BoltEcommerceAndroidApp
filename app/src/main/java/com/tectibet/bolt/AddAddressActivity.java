package com.tectibet.bolt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    private EditText mName;
    private EditText mCity;
    private EditText mAddress;
    private EditText mCode;
    private EditText mNumber;
    private Button mAddAddressbtn;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        mName=findViewById(R.id.ad_name);
        mCity=findViewById(R.id.ad_city);
        mAddress=findViewById(R.id.ad_address);
        mCode=findViewById(R.id.ad_code);
        mNumber=findViewById(R.id.ad_phone);
        mAddAddressbtn=findViewById(R.id.ad_add_address);
        mStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mToolbar=findViewById(R.id.add_address_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAddAddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=mName.getText().toString();
                String city=mCity.getText().toString();
                String address=mAddress.getText().toString();
                String code=mCode.getText().toString();
                String number=mNumber.getText().toString();
                String final_address="";
                if(!name.isEmpty()){
                    final_address+=name+", ";
                }
                if(!city.isEmpty()){
                    final_address+=city+", ";
                }
                if(!address.isEmpty()){
                    final_address+=address+", ";
                }
                if(!code.isEmpty()){
                    final_address+=code+", ";
                }
                if(!number.isEmpty()){
                    final_address+=number+", ";
                }
                Map<String,String> mMap=new HashMap<>();
                mMap.put("address",final_address);

                mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                        .collection("Address").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddAddressActivity.this, "Address Added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        });
    }
}
