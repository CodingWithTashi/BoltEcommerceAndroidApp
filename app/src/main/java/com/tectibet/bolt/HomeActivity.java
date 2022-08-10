package com.tectibet.bolt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tectibet.bolt.adapter.ItemsRecyclerAdapter;
import com.tectibet.bolt.domain.Items;
import com.tectibet.bolt.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Fragment homeFragment;
    private Toolbar mToolbar;
    private  FirebaseAuth mAuth;
    private EditText mSearchtext;
    private FirebaseFirestore mStore;
    private List<Items> mItemsList;
    private RecyclerView mItemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeFragment=new HomeFragment();
        loadFragment(homeFragment);
        mAuth=FirebaseAuth.getInstance();
        mToolbar=findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        mSearchtext=findViewById(R.id.search_text);
        mStore=FirebaseFirestore.getInstance();
        mItemsList=new ArrayList<>();
        mItemRecyclerView=findViewById(R.id.search_recycler);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemsRecyclerAdapter=new ItemsRecyclerAdapter(this,mItemsList);
        mItemRecyclerView.setAdapter(itemsRecyclerAdapter);
        mSearchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchItem(s.toString());

            }
        });
//        Button btn=findViewById(R.id.logout);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(HomeActivity.this,MainActivity.class));
//                finish();
//            }
//        });
    }

    private void searchItem(String text) {
        if(!text.isEmpty()){
            mStore.collection("All").whereGreaterThanOrEqualTo("name",text).get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful() && task.getResult()!=null){
                            mItemsList.clear();
                            for(DocumentSnapshot doc:task.getResult().getDocuments()){
                                Items items=doc.toObject(Items.class);
                                if(!mItemsList.contains(items)){
                                    mItemsList.add(items);

                                }
                            }
                            itemsRecyclerAdapter=new ItemsRecyclerAdapter(getApplicationContext(),mItemsList);
                            mItemRecyclerView.setAdapter(itemsRecyclerAdapter);
                            itemsRecyclerAdapter.notifyDataSetChanged();

                        }
                    });
        }else{
            mItemsList.clear();
            itemsRecyclerAdapter=new ItemsRecyclerAdapter(getApplicationContext(),new ArrayList<>());
            mItemRecyclerView.setAdapter(itemsRecyclerAdapter);
            itemsRecyclerAdapter.notifyDataSetChanged();
        }



    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container,homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout_btn){
            mAuth.signOut();
            Intent intent=new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId()==R.id.cart){
            Intent intent=new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
