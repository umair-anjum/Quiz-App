package com.example.quiz.Classes;

public class FinalResult {
    private int Question_Correct;
    private String User;
    private String CategoryId;
    private String quizId;

    public FinalResult() {
    }

    public FinalResult(int question_Correct, String user, String quizId) {
        Question_Correct = question_Correct;
        User = user;
        this.quizId=quizId;
    }

    public int getQuestion_Correct() {
        return Question_Correct;
    }

    public void setQuestion_Correct(int question_Correct) {
        Question_Correct = question_Correct;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
