package com.example.libor.lt1_danniel_isiah_libor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    EditText name;
    Spinner breed;
    Switch vaccination;
    RadioButton male, female;
    RadioGroup gender;
    ImageButton add, delete;
    String getBreed, getGender;
    ArrayAdapter adapterDogs;
    ListView lstviewDogs;
    Animation onClick, onLaunch;
    ArrayList<String> dogGender, dogBreed;
    boolean update = false;
    public int valueID[];
    public Integer cntrDog = 0;
    private float x1,x2,y1,y2;

    private DataBaseHelper dbhelper = new DataBaseHelper(MainActivity.this, "DogsDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        breed = (Spinner) findViewById(R.id.breed);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton)findViewById(R.id.female);
        gender = (RadioGroup)findViewById(R.id.gender);
        vaccination = (Switch) findViewById(R.id.vac);
        add = (ImageButton) findViewById(R.id.add);
        delete = (ImageButton) findViewById(R.id.delete);
        lstviewDogs = (ListView) findViewById(R.id.ListViewDogs);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.breed, R.layout.spinner_item);
        breed.setAdapter(adapter);

        onClick = AnimationUtils.loadAnimation(this, R.anim.alpha);
        onLaunch = AnimationUtils.loadAnimation(this, R.anim.launch);

        dogGender = new ArrayList();
        dogBreed = new ArrayList();

        name.startAnimation(onLaunch);
        breed.startAnimation(onLaunch);
        male.startAnimation(onLaunch);
        female.startAnimation(onLaunch);
        vaccination.startAnimation(onLaunch);
        add.startAnimation(onLaunch);
        delete.startAnimation(onLaunch);
        lstviewDogs.startAnimation(onLaunch);

        refreshall();
        longclick();
        lstview();
        name();
        breed();
        vac();
        gender();
        btnAddListener();
        btnDeleteListener();

    }

    private void lstview()
    {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        lstviewDogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    cntrDog=valueID[i];
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try
                {
                    String query = "Select * FROM tbldogs WHERE id = '" + cntrDog + "'";
                    Cursor cursor = db.rawQuery(query, null);

                    if (cursor.moveToFirst()) {
                        cursor.moveToFirst();
                        Toast.makeText(MainActivity.this,"Name: " + cursor.getString(1)
                                + "\nBreed: " + cursor.getString(2) + "\ndogInfo: " + cursor.getString(3)
                                + "\n" + cursor.getString(4),Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                lstviewDogs.setSelector(R.color.lstview);
            }
        });
    }

    private void refreshall()
    {
        setdefault();
        reloadDogs();
        reloadGenderSummary();
        reloadBreedSummary();
    }

    private dogInfo getGender()
    {
        dogInfo doginfo =new dogInfo();
        doginfo.setGender(dogGender);
        return doginfo;
    }

    private dogInfo getBreed()
    {
        dogInfo doginfo =new dogInfo();
        doginfo.setBreed(dogBreed);
        return doginfo;
    }

    private void sendData() {

        Intent intent=new Intent(this,Summary.class);
        intent.putExtra("gender",this.getGender());
        intent.putExtra("breed",this.getBreed());
        startActivity(intent);
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }

    private void longclick()
    {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        lstviewDogs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    cntrDog=valueID[i];
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("WARNING!");
                alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("UPDATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        update = true;
                        add.setImageResource(R.drawable.save);
                        delete.setImageResource(R.drawable.cancel);

                        try
                        {
                            String query = "Select * FROM tbldogs WHERE id = '" + cntrDog + "'";
                            Cursor cursor = db.rawQuery(query, null);

                            if (cursor.moveToFirst()) {
                                cursor.moveToFirst();
                                name.setText(cursor.getString(1));
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                alertDialog.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String sqlStr = "DELETE from tbldogs where id = '" + cntrDog + "'";
                        db.execSQL(sqlStr);
                        refreshall();
                    }
                }); alertDialog.show();

                return false;
            }
        });
    }

    private void name()
    {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(name.length() != 0) {
                } else {
                    name.setError("This field cannot be blank!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setdefault()
    {
        name.setText("");
        breed.setSelection(0);
        male.setChecked(true);
        vaccination.setChecked(false);
        getGender = "MALE";
        add.setImageResource(R.drawable.add);
        delete.setImageResource(R.drawable.delete);
        update = false;
    }

    private void breed() {
        breed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getBreed = breed.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void gender() {
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch(i)
                {
                    case R.id.male: getGender = "MALE"; break;
                    case R.id.female: getGender = "FEMALE"; break;
                }
            }
        });
    }

    private void vac() {
        vaccination.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == false) {
                    vaccination.setText("Vaccinated");
                    vaccination.setTrackResource(R.drawable.track);
                    vaccination.setThumbResource(R.drawable.thumb);
                } else if (b == true) {
                    vaccination.setText("Not Vaccinated");
                    vaccination.setTrackResource(R.drawable.trackonclick);
                    vaccination.setThumbResource(R.drawable.thumbonclick);
                }
            }
        });
    }

    private void btnDeleteListener() {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        final Cursor dogs = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tbldogs'", null);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete.startAnimation(onClick);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                if(dogs.getCount() == 0)
                {
                    Toast.makeText(MainActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(update == true)
                    {
                        refreshall();
                    }
                    else
                    {
                        alertDialog.setTitle("Confirm Delete...");

                        alertDialog.setMessage("Are you sure you want delete all records?");
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                                String sqlStr = "DELETE from tbldogs";

                                db.execSQL(sqlStr);
                                refreshall();
                            }
                        });
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                }

            }

        });
    }

    private void btnAddListener() {

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.startAnimation(onClick);

                if(name.length() == 0)
                {
                    Toast.makeText(MainActivity.this,"Missing field",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(update == true)
                    {
                        update();
                        refreshall();
                    }
                    else
                    {
                        insert();
                        refreshall();
                    }
                }
            }
        });

    }

    private void insert()
    {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            String sqlStr = "INSERT INTO tbldogs (dogName, dogBreed, dogGender, dogVac) VALUES ('"
                    + name.getText().toString() + "', '" + getBreed + "', '" + getGender
                    + "', '" + vaccination.getText().toString() + "')";
            db.execSQL(sqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update()
    {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {

            String sqlStr = "UPDATE tbldogs SET dogName = '" + name.getText().toString()
                    + "', dogBreed = '" + getBreed + "', dogGender = '" + getGender
                    + "' where id = '" + cntrDog + "'";
            db.execSQL(sqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadGenderSummary()
    {
        SQLiteDatabase dbSUm = dbhelper.getWritableDatabase();
        //get table from sqlite_master
        Cursor cDoginfo = dbSUm.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tbldogs'", null);
        cDoginfo.moveToNext();
        if (cDoginfo.getCount() == 0) { //check if the database is exisitng
            SQLite.FITCreateTable("DogsDatabase", this, "tbldogs", "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "dogName VARCHAR(90),dogBreed VARCHAR(90),dogGender VARCHAR(90),dogVac VARCHAR(90)"); //create table
        }
        cDoginfo = dbSUm.rawQuery("SELECT dogGender,count(dogGender) as total FROM tbldogs group by dogGender", null);
        String valueSum[] = new String[cDoginfo.getCount()];
        int ctrl = 0;
        dogGender.clear();
        while (cDoginfo.moveToNext()) {
            String strFor = "";
            strFor += System.lineSeparator() + cDoginfo.getString(cDoginfo.getColumnIndex("dogGender"));
            strFor += System.lineSeparator() + "Total : " + cDoginfo.getString(cDoginfo.getColumnIndex("total"));
            valueSum[ctrl] = strFor;
            dogGender.add(strFor);
            ctrl++;
        }
    }

    private void reloadBreedSummary()
    {
        SQLiteDatabase dbbreed = dbhelper.getWritableDatabase();
        //get table from sqlite_master
        Cursor cDoginfo = dbbreed.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tbldogs'", null);
        cDoginfo.moveToNext();
        if (cDoginfo.getCount() == 0) { //check if the database is exisitng
            SQLite.FITCreateTable("DogsDatabase", this, "tbldogs", "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "dogName VARCHAR(90),dogBreed VARCHAR(90),dogGender VARCHAR(90),dogVac VARCHAR(90)"); //create table
        }
        cDoginfo = dbbreed.rawQuery("SELECT dogBreed,count(*) as total, SUM(CASE WHEN dogGender = 'MALE' THEN 1 ELSE 0 END) AS male\n" +
                "        , SUM(CASE WHEN dogGender = 'FEMALE' THEN 1 ELSE 0 END) AS female FROM tbldogs group by dogBreed", null);
        String valueBreed[] = new String[cDoginfo.getCount()];
        int ctrl = 0;
        dogBreed.clear();
        while (cDoginfo.moveToNext()) {
            String strFor = "";
            strFor += System.lineSeparator() + "Breed : " + cDoginfo.getString(cDoginfo.getColumnIndex("dogBreed"));
            strFor += System.lineSeparator() + "Male : " + cDoginfo.getString(cDoginfo.getColumnIndex("male"));
            strFor += System.lineSeparator() + "Female : " + cDoginfo.getString(cDoginfo.getColumnIndex("female"));
            strFor += System.lineSeparator() + "Total : " + cDoginfo.getString(cDoginfo.getColumnIndex("total"));
            valueBreed[ctrl] = strFor;
            dogBreed.add(strFor);
            ctrl++;
        }
    }


    private void reloadDogs() {

        SQLiteDatabase dbDogs = dbhelper.getWritableDatabase();
        //get table from sqlite_master
        Cursor dogs = dbDogs.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tbldogs'", null);
        dogs.moveToNext();
        if (dogs.getCount() == 0) { //check if the database is exisitng
            SQLite.FITCreateTable("DogsDatabase", this, "tbldogs", "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "dogName VARCHAR(90),dogBreed VARCHAR(90),dogGender VARCHAR(90),dogVac VARCHAR(90)"); //create table

        } else {
            dogs = dbDogs.rawQuery("SELECT id, dogName, dogBreed, dogGender, dogVac  FROM tbldogs order by id desc", null);


            String valueDogs[] = new String[dogs.getCount()];
            int valueCurrentId[] = new int [dogs.getCount()];

            int ctrl = 0;
            while (dogs.moveToNext()) {
                String strFor = "";
                Integer strId;
                strFor += "Name : " + dogs.getString(dogs.getColumnIndex("dogName"));
                strFor += System.lineSeparator() + "dogInfo : " + dogs.getString(dogs.getColumnIndex("dogGender"));
                strFor += System.lineSeparator() + "Breed : " + dogs.getString(dogs.getColumnIndex("dogBreed"));
                strFor += System.lineSeparator() + "Vaccination : " + dogs.getString(dogs.getColumnIndex("dogVac"));
                strId = dogs.getInt(dogs.getColumnIndex("id"));
                valueDogs[ctrl] = strFor;
                valueCurrentId[ctrl] = strId;

                ctrl++;
            }
            valueID = Arrays.copyOf(valueCurrentId,dogs.getCount());
            adapterDogs = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, valueDogs);
            try {
                lstviewDogs.setAdapter(adapterDogs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch(touchevent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if(x1 < x2 && dogGender.size()!=0)
                {
                    sendData();
                }
                break;
            case MotionEvent.ACTION_UP:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
        }
        return false;
    }
}