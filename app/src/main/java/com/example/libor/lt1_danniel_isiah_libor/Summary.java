package com.example.libor.lt1_danniel_isiah_libor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Summary extends AppCompatActivity {

    ListView lstGender, lstBreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        lstGender = (ListView)findViewById(R.id.lstGender);
        lstBreed = (ListView)findViewById(R.id.lstBreed);

        receiveData();
    }

    private void  receiveData()
    {
        Intent i=this.getIntent();
        dogInfo dogBreed = (dogInfo) i.getSerializableExtra("breed");
        dogInfo dogGender = (dogInfo) i.getSerializableExtra("gender");
        lstBreed.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, dogBreed.getDogBreed()));
        lstGender.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, dogGender.getDogGender()));

    }

    @Override
    public void finish()
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}