package com.deepesh.firstaotproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_on,btn_off;
    ImageView color_disp;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_on=findViewById(R.id.btn_on);
        btn_off=findViewById(R.id.btn_off);
        color_disp=findViewById(R.id.color_disp);
        btn_on.setOnClickListener(this);
        btn_off.setOnClickListener(this);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("LED_STATUS");
        fAuth=FirebaseAuth.getInstance();

    }

    private void ledStatus(final int status){
        fAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                final int val=status;
                myRef.setValue(val);
            }
        });

    }

    public void getStatus(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value=dataSnapshot.getValue(Long.class);
                if (value==1){
                    color_disp.setBackgroundColor(getResources().getColor(R.color.on));
                }else {
                    color_disp.setBackgroundColor(getResources().getColor(R.color.off));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"FAIL ",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btn_on:
                ledStatus(1);
                getStatus();
                break;
            case R.id.btn_off:
                ledStatus(0);
                getStatus();
                break;
        }
    }
}
