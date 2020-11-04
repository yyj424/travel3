package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlanAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<MyPlan> myPlanList;
    private ArrayList<Group> groupList;
    private LayoutInflater layoutInflater;
    private boolean isGroup;

    public PlanAdapter(Context context, int layout, ArrayList<MyPlan> myPlanList) {
        this.context = context;
        this.layout = layout;
        this.myPlanList = myPlanList;
        isGroup = false;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public PlanAdapter(Context context, int layout, boolean isGroup, ArrayList<Group> groupList) {
        this.context = context;
        this.layout = layout;
        this.groupList = groupList;
        this.isGroup = isGroup;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (!isGroup) {
            return myPlanList.size();
        }
        else {
            return groupList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (!isGroup) {
            return myPlanList.get(position);
        }
        else {
            return groupList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);
        }

        TextView name = convertView.findViewById(R.id.y_planName);
        TextView start = convertView.findViewById(R.id.y_plan_startDate);
        TextView end = convertView.findViewById(R.id.y_plan_endDate);


        if (!isGroup) {
            name.setText(myPlanList.get(pos).getPlanName());
            start.setText(myPlanList.get(pos).getStartDate());
            end.setText(myPlanList.get(pos).getEndDate());
        }
        else {
            name.setText(groupList.get(pos).getGroupName());
            start.setText(groupList.get(pos).getStartDate());
            end.setText(groupList.get(pos).getEndDate());
        }

        return convertView;
    }
}
