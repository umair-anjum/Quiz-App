package com.example.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.Classes.User1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

   // SweetAlertDialog sd;
    EditText editNewRollno,editNewUser, editNewPassword, editNewEmail,editNewPhone;
    EditText edituser,password;
    Button signup ,btnSignIn;

    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup=findViewById(R.id.btnsSgnup);
        btnSignIn=findViewById(R.id.btnlogin);
        edituser=findViewById(R.id.Rollno);
        password=findViewById(R.id.Password);
        users = FirebaseDatabase.getInstance().getReference("Users");

        //Rollno Error
        edituser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(edituser.getText().toString().trim().equals("")){
                    edituser.setError("Enter Roll No");
                }
            }
        });
        //Password error
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(password.getText().toString().trim().length()<6){
                    password.setError("Enter 6 digit Password");
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edituser.getText().toString().trim().equals("")||password.getText().toString().trim().equals("")){
                       Toast.makeText(MainActivity.this, "Please Enter Roll No OR Password", Toast.LENGTH_SHORT).show();
                    }
                else if(edituser.getText().toString().trim().equals("admin")&&password.getText().toString().trim().equals("admin")){
                    Intent intent=new Intent(MainActivity.this,Admin.class);
                    startActivity(intent);
                }
                    else {

                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(edituser.getText().toString()).exists()) {
                                    User1 user = dataSnapshot.child(edituser.getText().toString()).getValue(User1.class);
                                    if (user.getPassword().equals(password.getText().toString())) {
                                        Intent intent = new Intent(MainActivity.this,StartIn.class);
                                        Common.currentUser=user;
                                        startActivity(intent);
                                        finish();
                                    } else
                                        Toast.makeText(MainActivity.this, "Password Wrong", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(MainActivity.this, "Please Register", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
               }



        });
    }

    private void showDialog() {

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
        alertdialog.setTitle("Sign Up").setCancelable(false);
        alertdialog.setMessage("Please fill the credentials");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sign_up, null);
        editNewEmail = view.findViewById(R.id.newemail);
        editNewUser = view.findViewById(R.id.newUsername);
        editNewPassword = view.findViewById(R.id.newpassword);
        editNewRollno=view.findViewById(R.id.newRollno);
        editNewPhone=view.findViewById(R.id.newPhone);
        alertdialog.setView(view);
        // alertdialog.setIcon(R.drawable.ic_account_circle_black_24dp);
        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editNewRollno.getText().toString().matches("")||editNewUser.getText().toString().matches("")||
                        editNewEmail.getText().toString().matches("")||editNewPassword.getText().toString().matches("")||
                         editNewPhone.getText().toString().matches("")) {
                    Toast.makeText(MainActivity.this, "Please Fill All Credentials", Toast.LENGTH_SHORT).show();
                }
                else {

                final User1 user = new User1(editNewRollno.getText().toString(), editNewUser.getText().toString()
                        , editNewEmail.getText().toString(), editNewPassword.getText().toString()
                        , editNewPhone.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserid()).exists()) {
                            Toast.makeText(MainActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                        } else {
                            users.child(user.getUserid())
                                    .setValue(user);
                            Toast.makeText(MainActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();
            }
            }
        });

        alertdialog.show();

        }

}
