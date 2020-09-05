package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlanAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<MyPlan> myPlanList;
    private LayoutInflater layoutInflater;

    public PlanAdapter(Context context, int layout, ArrayList<MyPlan> myPlanList) {
        this.context = context;
        this.layout = layout;
        this.myPlanList = myPlanList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myPlanList.size();
    }

    @Override
    public Object getItem(int position) {
        return myPlanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myPlanList.get(position).get_id();
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

        name.setText(myPlanList.get(pos).getPlanName());
        start.setText(myPlanList.get(pos).getStartDate());
        end.setText(myPlanList.get(pos).getEndDate());

        return convertView;
    }
}
