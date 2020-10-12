package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder> {

    private ArrayList<MyCourse> courseList;
    private Context context;
    private View.OnLongClickListener onLongClickItem;

    public PlaceListAdapter(Context context, ArrayList<MyCourse> courseList, View.OnLongClickListener onLongClickItem) {
        this.context = context;
        this.courseList = courseList;
        this.onLongClickItem = onLongClickItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.placelist_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String num = Long.valueOf(courseList.get(position).get_id()).toString();
        String name = courseList.get(position).getPlaceName();
        String memo = courseList.get(position).getMemo();

        holder.num.setText(num);
        holder.name.setText(name);
        holder.memo.setText(memo);
    }

    @Override
    public int getItemCount() {
        if (courseList != null)
            return courseList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView num;
        public EditText memo;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.y_placeName);
            num = itemView.findViewById(R.id.y_placeNum);
            memo = itemView.findViewById(R.id.y_placeMemo);
        }
    }
}
