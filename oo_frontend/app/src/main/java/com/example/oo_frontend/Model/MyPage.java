// ✅ 마이페이지 응답 모델 (서버 응답 구조 기준)
package com.example.oo_frontend.Model;

import java.util.List;
import com.example.oo_frontend.Model.ScheduleItem;

public class MyPage {
    private int userId;
    private String name; // 닉네임
    private String profileImage;
    private float rating;
    private int saleCount;
    private int purchaseCount;
    private int warningCount;
    private String reportReason;
    private String message; // 신고 메시지
    private List<ScheduleItem> scheduleInfo;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public int getSaleCount() { return saleCount; }
    public void setSaleCount(int saleCount) { this.saleCount = saleCount; }

    public int getPurchaseCount() { return purchaseCount; }
    public void setPurchaseCount(int purchaseCount) { this.purchaseCount = purchaseCount; }

    public int getWarningCount() { return warningCount; }
    public void setWarningCount(int warningCount) { this.warningCount = warningCount; }

    public String getReportReason() { return reportReason; }
    public void setReportReason(String reportReason) { this.reportReason = reportReason; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<ScheduleItem> getScheduleInfo() { return scheduleInfo; }
    public void setScheduleInfo(List<ScheduleItem> scheduleInfo) { this.scheduleInfo = scheduleInfo; }
}