package com.example.oo_frontend.Model;

public class ScheduleDto {
    private Long userId;
    private String day;
    private String startTime;
    private String endTime;
    private String subject;
    private String professor;

    public ScheduleDto(Long userId, String day, String startTime, String endTime, String subject, String professor) {
        this.userId = userId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
        this.professor = professor;
    }

    // 👇 꼭 필요한 userId getter/setter
    public Long getUserId() {
        return userId;
    }

    public void setUser(Long userId) {
        this.userId = userId;
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
