package com.example.todor.carme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SellActivity extends AppCompatActivity {

    private static final String NAME_KEY = "Name";
    private static final String PRICE_KEY = "Price";
    private static final String URL_KEY = "URL";

    FirebaseFirestore db;

    EditText name;
    EditText price;
    EditText pic;

    Button addCarButton;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    LinearLayout linearAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);
        setTitle("Sell");

        //writing to the database, based on user input
        db = FirebaseFirestore.getInstance();

        addCarButton = (Button) findViewById(R.id.button_submit);

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = findViewById(R.id.carName);
                price = findViewById(R.id.carPrice);
                pic = findViewById(R.id.carURL);

                String nName = name.getText().toString();
                String nPrice = price.getText().toString();
                String nPic = pic.getText().toString();

                Map<String, Object> newCar = new HashMap<>();

                newCar.put(NAME_KEY, nName);
                newCar.put(PRICE_KEY, nPrice);
                newCar.put(URL_KEY, nPic);


                db.collection("cars").document().set(newCar)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SellActivity.this, "Your vehicle is on the market!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SellActivity.this, "ERROR" + e.toString(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d("TAG", e.toString());
                            }
                        });
            }
        });


        //Drawer code
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //sets up the nav bar button

        //the main activity is the buy tab

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_menu);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.sell):
                        Intent sellIntent = new Intent(getApplicationContext(), SellActivity.class); //starts the sell activity after its clicked in nav menu
                        startActivity(sellIntent);
                        break;
                    case (R.id.buy):
                        Intent buyIntent = new Intent(getApplicationContext(), MainActivity.class); //starts the buy activity after its clicked in nav menu
                        startActivity(buyIntent);
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){ //allows to press nav bar button to toggle between main screen and nav bar
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
