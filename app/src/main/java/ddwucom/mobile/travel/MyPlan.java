package ddwucom.mobile.travel;

public class MyPlan {
    private long _id;
    private String PlanName;
    private String StartDate;
    private String EndDate;

    public MyPlan(long _id, String planName, String startDate, String endDate) {
        this._id = _id;
        PlanName = planName;
        StartDate = startDate;
        EndDate = endDate;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
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
}
