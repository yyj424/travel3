package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class MyFriendsAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> myDataList;
   //private String[] myDataList;
    private LayoutInflater layoutInflater;

    public MyFriendsAdapter(Context context, int layout, ArrayList myDataList) {
        this.context = context;
        this.layout = layout;
        this.myDataList = myDataList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return myDataList.size();
    }
    @Override
    public Object getItem(int pos) {
        return myDataList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final int pos = position;
        MyFriendsAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, viewGroup, false);

            holder = new MyFriendsAdapter.ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.isAdd = (CheckBox) convertView.findViewById(R.id.tv_is_add);
            convertView.setTag(holder);
        } else {
            holder = (MyFriendsAdapter.ViewHolder) convertView.getTag();
        }
        holder.name.setText(myDataList.get(pos));
        //holder.isAdd.setClickable(false);
        //holder.isAdd.set
        return convertView;
    }
    static class ViewHolder {
        TextView name;
        CheckBox isAdd;
    }
}
