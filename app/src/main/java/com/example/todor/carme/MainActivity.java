package com.example.todor.carme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Integer i;
    FrameLayout carFrame;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Buy");

        db = FirebaseFirestore.getInstance();

        //receives data from the database, in this case all the documents in the cars collection that was added by the sale activity


        carFrame = findViewById(R.id.carFrame);
        
            for(i = 0; i < 10; i++) {
            //multiple doc call
            db.collection("cars")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    TextView name = findViewById(R.id.carName);
                                    TextView price = findViewById(R.id.carPrice);

                                    name.append(document.get("Name").toString());
                                    price.append(document.get("Price").toString());
                                }
                            }
                        }
                    });

            }


        //single doc code :working
        /*DocumentReference data = db.collection("cars").document("Car1");

        data.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            TextView name = findViewById(R.id.carName);
                            TextView price = findViewById(R.id.carPrice);

                            name.append(doc.get("Name").toString());
                            price.append(doc.get("Price").toString());

                        }
                    }
                });*/


        mDrawerLayout = findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //sets up the nav bar button

        //the main activity is the buy tab

        NavigationView mNavigationView = findViewById(R.id.nav_menu);
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
