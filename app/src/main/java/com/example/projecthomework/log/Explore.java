package com.example.projecthomework.log;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.projecthomework.R;
import com.example.projecthomework.giriş.MainActivity;
import com.example.projecthomework.map.MapsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Explore extends AppCompatActivity {

    TextView textView8;
    ImageView imageView3;

   EditText editText7;
    EditText editText8;
    EditText editText11;
    EditText editText12;
    EditText editText13;

    Spinner spinner;
    String selectedCategory=" ";

    FirebaseFirestore firebaseFirestore;

    FirebaseAuth auth;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);


        textView8=findViewById(R.id.textview8);
        imageView3=findViewById(R.id.imageView3);
        editText7=findViewById(R.id.editTextText7);
        editText8=findViewById(R.id.editTextText8);
        editText11=findViewById(R.id.editTextText11);
        editText12=findViewById(R.id.editTextText12);
        editText13=findViewById(R.id.editTextText13);
        spinner=findViewById(R.id.spinner);
        firebaseFirestore=FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();



        @SuppressLint("ResourceType")
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Places,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PopupMenu popupMenu = new PopupMenu(this, imageView3);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Sign out");
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 0) {
                FirebaseAuth.getInstance().signOut();
                textView8.setText("Sign out");
                startActivity(new Intent(Explore.this, MainActivity.class));
                finish();
                return true;
            }
            return false;
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }

    public void explore(View view){

        String city=editText7.getText().toString().trim();
        String town=editText8.getText().toString().trim();
        String keyword1=editText11.getText().toString().trim();
        String keyword2=editText12.getText().toString().trim();
        String keyword3=editText13.getText().toString().trim();


        if(city.isEmpty() || town.isEmpty() || keyword1.isEmpty()||keyword2.isEmpty()||keyword3.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String,Object> data=new HashMap<>();


        data.put("city",city);
        data.put("town",town);
        data.put("category",selectedCategory);
        data.put("keyword1",keyword1);
        data.put("keyword2",keyword2);
        data.put("keyword3",keyword3);





        firebaseFirestore.collection("Data").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Intent intent = new Intent(Explore.this, MapsActivity.class);
                intent.putExtra("city", city);
                intent.putExtra("town", town);
                intent.putExtra("category", selectedCategory);
                intent.putExtra("keyword1", keyword1);
                intent.putExtra("keyword2", keyword2);
                intent.putExtra("keyword3", keyword3);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Explore.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }

}