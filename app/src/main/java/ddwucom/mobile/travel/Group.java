package ddwucom.mobile.travel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Group implements Serializable {
    private ArrayList<String> members;
    private String gid;
    private String groupName;
    private String startDate;
    private String endDate;
    private Map<String, Object> daysList;

    public Group() {
    }

    public Group(ArrayList<String> members, String groupName, String startDate, String endDate) {
        this.members = members;
        this.groupName = groupName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Group(ArrayList<String> members, String groupName, String startDate, String endDate, Map<String, Object> daysList) {
        this.members = members;
        this.groupName = groupName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.daysList = daysList;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<String, Object> getDaysList() {
        return daysList;
    }

    public void setDaysList(Map<String, Object> daysList) {
        this.daysList = daysList;
    }
}
