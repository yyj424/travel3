package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {

    private ArrayList<MyCourse> courseList;
    private Context context;
    private View.OnClickListener onClickItem;

    public CourseListAdapter(Context context, ArrayList<MyCourse> courseList, View.OnClickListener onClickItem) {
        this.context = context;
        this.courseList = courseList;
        this.onClickItem = onClickItem;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.courselist_adapter_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String num = Long.valueOf(courseList.get(position).get_id()).toString();
        String name = courseList.get(position).getPlaceName();

        holder.num.setText(num);
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView num;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.y_course_name);
            num = itemView.findViewById(R.id.y_course_num);
        }
    }
}
