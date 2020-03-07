package com.example.quiz;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.Classes.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements View.OnClickListener {
    int index = 0, thisQuestion = 0, totalQuestion=0, correctAnwer;
    private long Start_time_In_Millis=30000*Common.TotalQ;
    int seconds;
    TextView mCountdowntxt;
    private CountDownTimer mCountDown;
    private long mtimeLeftMillis=Start_time_In_Millis;
    final static long INTERVAL = 1000;
    final static long TIMEOUT = 10000;
     List<Question> list_question;
    int progressValue = 0;


    ProgressBar progressBar;
    Button btnA, btnB, btnC, btnD;
    TextView  txtQuestionNum, question_text;
    DatabaseReference questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
        list_question=new ArrayList<>();

        question_text=findViewById(R.id.txtQuestion);
        txtQuestionNum = findViewById(R.id.txtTotalQuestion);
        progressBar = findViewById(R.id.progressBar);
        mCountdowntxt=findViewById(R.id.Countdowntext);
        questions= FirebaseDatabase.getInstance().getReference("Questions");
        totalQuestion = list_question.size();
        Start_time_In_Millis=Start_time_In_Millis*totalQuestion;

        StartTimer();
    }

    @Override
    public void onClick(View v) {

        //still have question in list
        if (index <= totalQuestion) {
            Button clickedButton = (Button) v;
            if (clickedButton.getText().equals(Common.list_question.get(index).getCorrectAnswer())) {
                correctAnwer++;
                showQuestion(++index);

            }
           else {

                Toast.makeText(this, "index is:"+index+" T is"+totalQuestion , Toast.LENGTH_SHORT).show();
                showQuestion(++index);
            }
//            txtScore.setText(String.format("%d", score));

        }
        else {
            Intent intent = new Intent(this, Result.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnwer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();

        }

    }

    private void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));

                question_text.setText("Q. "+Common.list_question.get(index).getQuestion());
                question_text.setVisibility(View.VISIBLE);
            }

             else  {

            Intent intent = new Intent(this, Result.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnwer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
        try {
            btnA.setText(Common.list_question.get(index).getAnswerA());
            btnB.setText(Common.list_question.get(index).getAnswerB());
            btnC.setText(Common.list_question.get(index).getAnswerC());
            btnD.setText(Common.list_question.get(index).getAnswerD());

        }
        catch (Exception ex){
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        totalQuestion = Common.list_question.size();
        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue+=12;

            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);

            }
        };
        showQuestion(index);
    }

    public void StartTimer(){

        mCountDown=new CountDownTimer(mtimeLeftMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            mtimeLeftMillis=millisUntilFinished;
            UpdateTimeCounter();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
    public void UpdateTimeCounter(){

        int minutes = (int) (mtimeLeftMillis / 1000) / 60;
         seconds = (int) (mtimeLeftMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mCountdowntxt.setText(timeLeftFormatted);
        if(seconds==0&&minutes==0){
            Intent intent = new Intent(this, Result.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnwer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }
    private void resetTimer() {
       mtimeLeftMillis = Start_time_In_Millis;
        UpdateTimeCounter();
    }
}
