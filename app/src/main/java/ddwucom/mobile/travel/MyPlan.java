package ddwucom.mobile.travel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MyPlan {
    private String uid;
    private String PlanName;
    private String StartDate;
    private String EndDate;
    Map<String, Object> daysList;

    public MyPlan() {}

    public MyPlan(String planName, String startDate, String endDate) {
        PlanName = planName;
        StartDate = startDate;
        EndDate = endDate;
    }

    public MyPlan(String uid, String planName, String startDate, String endDate, Map<String, Object> daysList) {
        this.uid = uid;
        this.PlanName = planName;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.daysList = daysList;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public Map<String, Object> getDaysList() {
        return daysList;
    }

    public void setDaysList(Map<String, Object> daysList) {
        this.daysList = daysList;
    }
}
