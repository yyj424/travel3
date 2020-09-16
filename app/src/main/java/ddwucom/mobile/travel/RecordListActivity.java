package ddwucom.mobile.travel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordListActivity extends Activity {
    RecordAdapter recordAdapter;
    GridView gv;
    ArrayList<Record> recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        gv = findViewById(R.id.grid_record);
        //recordAdapter = new RecordAdapter(this, R.layout.record_grid_view, );
        gv.setAdapter(recordAdapter);
    }

    public class RecordAdapter extends BaseAdapter {
        Context context;
        private int layout;
        private ArrayList<Record> recordsList;
        private LayoutInflater layoutInflater;

        public RecordAdapter(Context c, int layout, ArrayList<Record> recordsList) {
            this.context = c;
            this.layout = layout;
            this.recordsList = recordsList;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
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
                holder.recordImage = convertView.findViewById(R.id.img_record);
                holder.tvRecordTitle = convertView.findViewById(R.id.tv_recordTitle);
                holder.tvRecordDate = convertView.findViewById(R.id.tv_recordDate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.recordImage.setImageResource(recordsList.get(pos).getImageResId());
            holder.tvRecordTitle.setText(recordsList.get(position).getRecordTitle());
            holder.tvRecordDate.setText(recordsList.get(position).getDate());

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView recordImage;
        TextView tvRecordTitle;
        TextView tvRecordDate;
    }
}

