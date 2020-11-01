package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyFriendsAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<UserInfo> memberList;
    private LayoutInflater layoutInflater;

    public MyFriendsAdapter(Context context, int layout, ArrayList<UserInfo> memberList) {
        this.context = context;
        this.layout = layout;
        this.memberList = memberList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return memberList.size();
    }
    @Override
    public Object getItem(int pos) {
        return memberList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final int pos = position;
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, viewGroup, false);

            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.tv_name);
            holder.btnDeleteMember = convertView.findViewById(R.id.btnDeleteMember);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(memberList.get(pos).getNickname());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        ImageButton btnDeleteMember;
    }
}
