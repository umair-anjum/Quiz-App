package com.example.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.Classes.Question;
import com.example.quiz.Classes.QuestionList;
import com.example.quiz.Classes.User1;
import com.example.quiz.Classes.UserList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {

    Spinner spn; String catid;
    EditText Question,op_A,op_B,op_C,op_D,Correct_op;
    TextView logout;
    Button addquestion;
    DatabaseReference questions;
    DatabaseReference users;
    List<Question> Questions;
    List<User1>Users;
    ListView HybridlistView,HybridlistView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

         addquestion=findViewById(R.id.AddQues);
         HybridlistView=findViewById(R.id.Qlist);
         HybridlistView2=findViewById(R.id.Qlist);
         logout=findViewById(R.id.Logout);
         spn=findViewById(R.id.spinner);
        Questions= new ArrayList<>();
        Users=new ArrayList<>();
         questions= FirebaseDatabase.getInstance().getReference("Questions");
         users= FirebaseDatabase.getInstance().getReference("Users");

        addquestion.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(spn.getSelectedItem().toString().equalsIgnoreCase("Select Quiz No")) {
                     Toast.makeText(Admin.this, "Please Select Quiz No", Toast.LENGTH_SHORT).show();
                 }
                 else {
                     catid=spn.getSelectedItem().toString();
                     showDialog(); 
                 }
             }
         });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,MainActivity.class);
                startActivity(intent);
            }
        });
        initialize();
        initializeUser();
        SetSpinner();


    }

    private void showDialog() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Admin.this);
        alertdialog.setTitle("Add Question");
        alertdialog.setMessage("Please fill the Fields");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.question_layout, null);
       Question=view.findViewById(R.id.AddQuestion);
       op_A=view.findViewById(R.id.addChoiceA);
       op_B=view.findViewById(R.id.addChoiceB);
       op_C=view.findViewById(R.id.addChoiceC);
       op_D=view.findViewById(R.id.addChoiceD);
       Correct_op=view.findViewById(R.id.CorrectChoice);
        alertdialog.setView(view);
       // alertdialog.setIcon(R.drawable.ic_account_circle_black_24dp);
        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
                
                final String id=questions.push().getKey();
               final Question question=new Question(id,Question.getText().toString() ,op_A.getText().toString(),op_B.getText().toString(),
                       op_C.getText().toString(),op_D.getText().toString(),Correct_op.getText().toString(),catid);

                if (!TextUtils.isEmpty(Question.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(op_A.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(op_B.getText().toString().trim())) {
                            if (!TextUtils.isEmpty(Correct_op.getText().toString().trim())) {
                                //Method for update data
                                questions.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child(id).exists()) {
                                            Toast.makeText(Admin.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                                        } else {
                                            questions.child(id).setValue(question);
                                            Toast.makeText(Admin.this, "Registration Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                dialog.dismiss();
                            }
                        }
                    }
                }



            }
        });
        alertdialog.show();

    }

    public void initialize(){


    }
    @Override
    protected void onStart() {
        super.onStart();


    }






    public void ShowQuestions(View view) {

        if(spn.getSelectedItem().toString().equalsIgnoreCase("Select Quiz No")) {
            Toast.makeText(Admin.this, "Please Select Quiz No", Toast.LENGTH_SHORT).show();
        }
        else {
            catid=spn.getSelectedItem().toString();
          //  showDialog();
        }
        questions.orderByChild("quizId").equalTo(spn.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous Questions list
                Questions.clear();

                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting Questions from firebase console
                    Question question = postSnapshot.getValue(Question.class);
                    //adding Questions to the list
                    Questions.add(question);
                }
                //creating QuestionList adapter
                QuestionList Adapter=new QuestionList(Admin.this,Questions);
                //attaching adapter to the listview
                HybridlistView.setAdapter(Adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        HybridlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Question question = Questions.get(i);

                UpdateAndDeleteDialog(question.getID(),question.getQuestion() ,question.getAnswerA(),question.getAnswerB(),question.getAnswerC(),
                        question.getAnswerD(),question.getCorrectAnswer());
                Toast.makeText(Admin.this, ""+question.getID(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void UpdateAndDeleteDialog(final String Qid, String Ques,String A, String B,String C, String D,String correct_choice) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_delete_dialog, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final EditText updateQuestion = (EditText) dialogView.findViewById(R.id.UpdateQuestion);
        final EditText update_choiceA = (EditText) dialogView.findViewById(R.id.UpdateChoiceA);
        final EditText update_ChoiceB = (EditText) dialogView.findViewById(R.id.UpdateChoiceB);
        final EditText update_choiceC = (EditText) dialogView.findViewById(R.id.UpdateChoiceC);
        final EditText update_ChoiceD = (EditText) dialogView.findViewById(R.id.UpdateChoiceD);
        final EditText update_correct_Choice = (EditText) dialogView.findViewById(R.id.UpdateCorrectChoice);
        //to set the text from database
        updateQuestion.setText(Ques);
        update_choiceA.setText(A);
        update_ChoiceB.setText(B);
        update_choiceC.setText(C);
        update_ChoiceD.setText(D);
        update_correct_Choice.setText(correct_choice);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btnUpdateQuestion);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.btnDeleteQuestion);
        //id for set dialog title
        dialogBuilder.setCancelable(false);;
        dialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog builder_X = dialogBuilder.create();
        builder_X.show();

        // Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ques = updateQuestion.getText().toString().trim();
                String a = update_choiceA.getText().toString().trim();
                String b = update_ChoiceB.getText().toString().trim();
                String c =update_choiceC.getText().toString().trim();
                String d = update_ChoiceD.getText().toString().trim();
                String correctChoice = update_correct_Choice.getText().toString().trim();
                //checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(ques)) {
                    if (!TextUtils.isEmpty(a)) {
                        if (!TextUtils.isEmpty(b)) {
                            if (!TextUtils.isEmpty(correctChoice)) {
                                //Method for update data
                                updateQuestion(Qid,ques,a,b,c,d,correctChoice);
                                Toast.makeText(Admin.this, "id is:"+catid, Toast.LENGTH_SHORT).show();
                                builder_X.dismiss();
                            }
                        }
                    }
                }


            }
        });

        // Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method for delete data
                deleteQuestion(Qid);
                builder_X.dismiss();
            }
        });
    }
      private boolean updateQuestion(String Qid, String Ques,String A, String B,String C, String D,String correct_choice) {
        //getting the specified Questions reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("Questions").child(Qid);
        Question question=new Question(Qid,Ques,A,B,C,D,correct_choice,catid);
        //update  Questions  to firebase
        UpdateReference.setValue(question);
        Toast.makeText(getApplicationContext(), "Question Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteQuestion(String id) {
        //getting the specified Questions reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("Questions").child(id);
        //removing Questions
        DeleteReference.removeValue();
        Toast.makeText(getApplicationContext(), "Question Deleted", Toast.LENGTH_LONG).show();
        return true;
    }
    public void ViewStudents(View view) {

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous User list
                Users.clear();

                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting User from firebase console
                    User1 User = postSnapshot.getValue(User1.class);
                    //adding User to the list
                    Users.add(User);
                }
                //creating Userlist adapter
                UserList UserAdapter = new UserList(Admin.this, Users);
                //attaching adapter to the listview
                HybridlistView2.setAdapter(UserAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        HybridlistView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User1 user = Users.get(i);
                Toast.makeText(Admin.this, "i is =" + i, Toast.LENGTH_SHORT).show();
                UserUpdateAndDeleteDialog(user.getUserid(), user.getUsername(), user.getUseremail(), user.getPassword(), user.getUsermobileno());
                Toast.makeText(Admin.this, "" + user.getUserid(), Toast.LENGTH_SHORT).show();
            }
        });
    }
        public void initializeUser () {

        }


    private void UserUpdateAndDeleteDialog(final String rollno, String username, final String email, String password,String phone) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_delete_user, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final EditText updateTextname = (EditText) dialogView.findViewById(R.id.UpdateName);
        final EditText updateTextemail = (EditText) dialogView.findViewById(R.id.UpdateEmail);
        final EditText updateTextmobileno = (EditText) dialogView.findViewById(R.id.UpdatePhone);
        final EditText updateTextPassword = (EditText) dialogView.findViewById(R.id.UpdatePassword);
        //
        updateTextname.setText(username);
        updateTextemail.setText(email);
        updateTextmobileno.setText(phone);
        updateTextPassword.setText(password);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btnUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.btnDeleteUser);
        //username for set dialog title
        dialogBuilder.setTitle(rollno).setCancelable(false);
        dialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog b = dialogBuilder.create();

        b.show();

        // Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateTextname.getText().toString().trim();
                String email = updateTextemail.getText().toString().trim();
                String mobilenumber = updateTextmobileno.getText().toString().trim();
                String passw = updateTextPassword.getText().toString().trim();
                //checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(email)) {
                        if (!TextUtils.isEmpty(mobilenumber)) {
                            if (!TextUtils.isEmpty(passw)) {
                                //Method for update data
                                updateUser(rollno, name, email, mobilenumber, passw);
                                b.dismiss();
                            }
                        }
                    }
                }

            }
        });

        // Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method for delete data
                deleteUser(rollno);
                b.dismiss();
            }
        });
    }
    private boolean updateUser(String id, String name, String email, String mobilenumber,String passWord) {
        //getting the specified User reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("Users").child(id);
        User1 User = new User1(id, name, email, mobilenumber,passWord);
        //update  User  to firebase
        UpdateReference.setValue(User);
        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id) {
        //getting the specified User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("Users").child(id);
        //removing User
        DeleteReference.removeValue();
        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    public void SetSpinner(){

        ArrayAdapter<String> mAdapter=new ArrayAdapter<String>(Admin.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.QuizNo));
            spn.setAdapter(mAdapter);
    }
}
