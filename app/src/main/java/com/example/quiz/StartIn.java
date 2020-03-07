package com.example.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quiz.Classes.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class StartIn extends AppCompatActivity {

    Button btnStart;
    FirebaseDatabase database;
    DatabaseReference questions;
    Spinner spn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_in);
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
        spn1=findViewById(R.id.spinner2);

        SetSpinner();

        btnStart = findViewById(R.id.btnPlay);
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Common.QuizId=spn1.getSelectedItem().toString();
                loadQuestion(Common.QuizId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spn1.getSelectedItem().toString().equalsIgnoreCase("Select Quiz No")){
                    Toast.makeText(StartIn.this, "Please select quiz NO", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(Common.list_question.size()==0){
                        Toast.makeText(StartIn.this, "Loading Questions Please Wait !!!!!", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Common.TotalQ=Common.list_question.size();
                        Intent intent = new Intent(StartIn.this, Home.class);
                        intent.putExtra("value", spn1.getSelectedItem().toString());
                        finish();
                        startActivity(intent);

                    }
                }
            }
        });


    }

    private void loadQuestion(String quizid) {
        // first clear list if have old questions
        if (Common.list_question.size() > 0) {
            Common.list_question.clear();
        }
        questions.orderByChild("quizId").equalTo(quizid)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Question ques = postSnapshot.getValue(Question.class);
                    Common.list_question.add(ques);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Collections.shuffle(Common.list_question);
    }

    public void SetSpinner(){

        ArrayAdapter<String> mAdapter=new ArrayAdapter<String>(StartIn.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.QuizNo));
        spn1.setAdapter(mAdapter);
    }
}
