package com.example.projecthomework.log;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projecthomework.R;
import com.example.projecthomework.databinding.ActivitySignUpBinding;
import com.example.projecthomework.giriş.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_up extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    CheckBox checkBoxTerms;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkBoxTerms = findViewById(R.id.checkBoxRemember);
        auth = FirebaseAuth.getInstance();

        // EditText’leri temizle
        binding.editTextText2.setText("");
        binding.editTextText3.setText("");
        binding.editTextText4.setText("");
        binding.editTextText5.setText("");


        binding.button3.setOnClickListener(v -> {
            String name = binding.editTextText2.getText().toString();
            String surname = binding.editTextText3.getText().toString();
            String phone_number = binding.editTextText4.getText().toString();
            String birthday = binding.editTextText5.getText().toString();
            String email = binding.editTextText6.getText().toString();
            String password = binding.editTextNumberPassword3.getText().toString();

            if (name.equals("") || surname.equals("") || phone_number.equals("")
                    || birthday.equals("") || email.equals("") || password.equals("")) {
                Toast.makeText(Sign_up.this, "Enter fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }

            if (!checkBoxTerms.isChecked()) {
                Toast.makeText(Sign_up.this, "You must accept the terms", Toast.LENGTH_LONG).show();
                return;
            }


            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Sign_up.this, "Kayıt başarılı", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Sign_up.this, Explore.class));
                            finish();
                        } else {
                            Toast.makeText(Sign_up.this, "Kayıt başarısız: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}


