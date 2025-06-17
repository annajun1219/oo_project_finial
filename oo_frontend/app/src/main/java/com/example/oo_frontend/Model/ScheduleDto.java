package com.example.oo_frontend.Model;

public class ScheduleDto {
    private User user;
    private String day;
    private String startTime;
    private String endTime;
    private String subject;
    private String professor;

    public ScheduleDto(User user, String day, String startTime, String endTime, String subject, String professor) {
        this.user = user;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
        this.professor = professor;
    }

    // 👇 꼭 필요한 userId getter/setter
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}