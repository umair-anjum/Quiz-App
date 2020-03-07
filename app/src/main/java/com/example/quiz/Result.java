package com.example.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quiz.Classes.FinalResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Result extends AppCompatActivity {

    TextView correctans,TotalQues;
    Button logout;
    FirebaseDatabase database;
    DatabaseReference quesiton_score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        correctans=findViewById(R.id.CorrectQues);
        TotalQues=findViewById(R.id.TotalQuestion);

        database = FirebaseDatabase.getInstance();
        quesiton_score = database.getReference("Question_Score");
        logout=findViewById(R.id.Logout);
        Common.list_question.clear();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Result.this, MainActivity.class);
                startActivity(intent);

            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

           correctans.setText(String.format(" Correct : %d", correctAnswer));
            TotalQues.setText(String.format(" Attempted : %d/%d", correctAnswer, totalQuestion));



            quesiton_score.child(String.format("%s_%s", Common.currentUser.getUserid(),
                    Common.QuizId))
                    .setValue(new FinalResult(correctAnswer,"",""));


        }



    }
}
