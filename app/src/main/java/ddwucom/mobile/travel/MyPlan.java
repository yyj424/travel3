package ddwucom.mobile.travel;

import java.util.List;

public class MyPlan {
    private String uid;
    private String PlanName;
    private String StartDate;
    private String EndDate;
    List<String> daysList;

    public MyPlan() {}

    public MyPlan(String uid, String planName, String startDate, String endDate, List<String> daysList) {
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

    public List<String> getDaysList() {
        return daysList;
    }

    public void setDaysList(List<String> daysList) {
        this.daysList = daysList;
    }
}
