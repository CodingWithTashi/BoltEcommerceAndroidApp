package com.tectibet.bolt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class PayStackActivity extends AppCompatActivity {
    private static final String TAG = PayStackActivity.class.getName();
    EditText cardNumberEditText;
    EditText expYear;
    EditText expMonth;
    EditText edit_cvc;
    EditText amountText;
    Button payBtn;
    Double amountDouble=null;
    String img_url = "";
    String name = "";
    private FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_stack);

        cardNumberEditText = findViewById(R.id.edit_card_number);
        expMonth = findViewById(R.id.edit_expiry_month);
        expYear = findViewById(R.id.edit_expiry_year);
        edit_cvc = findViewById(R.id.edit_cvc);
        amountText = findViewById(R.id.amountId);
        payBtn = findViewById(R.id.button_perform_local_transaction);
        amountDouble = getIntent().getDoubleExtra("amount",0.0);
        img_url = getIntent().getStringExtra("img_url");
        name = getIntent().getStringExtra("name");
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        payBtn.setOnClickListener(view->{
            //Test Card
            // cardNumber = "4084084084084081";
            // expiryMonth = 11;
            // expiryYear = 22;
            //String cvv = "408";  // cvv of the test card
            String cardNumber = cardNumberEditText.getText().toString();
            int expiryMonth = Integer.parseInt(expMonth.getText().toString()); //any month in the future
            int expiryYear = Integer.parseInt(expYear.getText().toString()); // any year in the future. '2018' would work also!
            String cvv = edit_cvc.getText().toString();  // cvv of the test card

            //init Card
            Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
            if (card.isValid()) {
                //perform payment
                performCharge(card);
            } else {
                Toast.makeText(this, "Not Valid", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void performCharge(Card card){
        //create a Charge object
        Charge charge = new Charge();
        // multiply by 100, since payment start from 0.00
        int amount = (int) (amountDouble*100);
        charge.setAmount(amount);
        charge.setCurrency("ZAR");
        charge.setEmail("codingwithtashi@support.com");
        charge.setCard(card); //sets the card to charge

        PaystackSdk.chargeCard(PayStackActivity.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                Toast.makeText(PayStackActivity.this, "Payment Successful" + transaction.getReference(), Toast.LENGTH_SHORT).show();
                Map<String, Object> mMap = new HashMap<>();
                mMap.put("name", name);
                mMap.put("img_url", img_url);
                mMap.put("payment_id", transaction.getReference());

                mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                        .collection("Orders").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
                Log.e(TAG, "beforeValidate: " );
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                Toast.makeText(PayStackActivity.this, ""+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: ",error );
            }

        });
    }
}