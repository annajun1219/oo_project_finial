package com.example.oo_frontend.Model;

import com.google.gson.annotations.SerializedName;

public class Recommendation {

    @SerializedName("subjectName")
    private String subjectName;

    @SerializedName("professor")
    private String professor;

    // 생성자
    public Recommendation(String subjectName, String professor) {
        this.subjectName = subjectName;
        this.professor = professor;
    }

    // Getter
    public String getSubjectName() {
        return subjectName;
    }

    public String getProfessor() {
        return professor;
    }
}
