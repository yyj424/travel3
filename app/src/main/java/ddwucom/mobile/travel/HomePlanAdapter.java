package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HomePlanAdapter extends RecyclerView.Adapter<HomePlanAdapter.ViewHolder> {
    private ArrayList<MyPlan> hplanList;
    private Context context;
    private OnItemClickListener planClick = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener planClick)
    {
        this.planClick = planClick;
    }

    public HomePlanAdapter(Context context, ArrayList<MyPlan> hplanList) {
        this.context = context;
        this.hplanList = hplanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.home_plans_card_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String name = hplanList.get(position).getPlanName();
        String start = hplanList.get(position).getStartDate();
        String end = hplanList.get(position).getEndDate();

        holder.name.setText(name);
        holder.start.setText(start);
        holder.end.setText(end);
    }

    @Override
    public int getItemCount() {
        if (hplanList != null)
            return hplanList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView start;
        public TextView end;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.y_card_planName);
            start = itemView.findViewById(R.id.y_card_startDate);
            end = itemView.findViewById(R.id.y_card_endDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(planClick != null) {
                            planClick.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }
}
