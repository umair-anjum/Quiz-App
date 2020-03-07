package com.example.quiz.Classes;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quiz.R;

import java.util.List;

public class QuestionList extends ArrayAdapter<Question> {

    private Activity context;
    //list of all questions
    List<Question> Questions;

    public QuestionList(Activity context, List<Question> Questions) {
        super(context, R.layout.question_layout, Questions);
        this.context = context;
        this.Questions = Questions;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_question_list, null, true);
        //initialize
        TextView textViewQuestion = (TextView) listViewItem.findViewById(R.id.ViewQuestion);
        TextView textviewOP_A = (TextView) listViewItem.findViewById(R.id.ViewOptionA);
        TextView textviewOP_B = (TextView) listViewItem.findViewById(R.id.ViewOptionB);
        TextView textviewOP_C = (TextView) listViewItem.findViewById(R.id.ViewOptionC);
        TextView textviewOP_D = (TextView) listViewItem.findViewById(R.id.ViewOptionD);
        TextView textviewOP_CorrectOp = (TextView) listViewItem.findViewById(R.id.ViewCorrectAns);

        //getting user at position
        Question question = Questions.get(position);
        //set Question
        textViewQuestion.setText(question.getQuestion());
        //set option A
        textviewOP_A.setText(question.getAnswerA());
        //set option B
        textviewOP_B.setText(question.getAnswerB());
        //set option C
        textviewOP_C.setText(question.getAnswerC());
        //set option D
        textviewOP_D.setText(question.getAnswerD());
        //set correct option
        textviewOP_CorrectOp.setText(question.getCorrectAnswer());


        return listViewItem;
    }
}