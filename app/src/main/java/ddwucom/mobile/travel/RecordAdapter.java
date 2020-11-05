package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private Context context;
    private List<Record> recordList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener mListener = null;
    private boolean isGroup;
    private boolean isRecordMain;

    public RecordAdapter(Context context, boolean isGroup, List<Record> recordList) {
        this.context = context;
        this.isGroup = isGroup;
        this.isRecordMain = false;
        this.recordList = recordList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public RecordAdapter(Context context, boolean isGroup, boolean isRecordMain, List<Record> recordList) {
        this.context = context;
        this.isGroup = isGroup;
        this.isRecordMain = isRecordMain;
        this.recordList = recordList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivRecord;
        public TextView tvRecordTitle;
        public TextView tvRecordDate;
        public TextView tvRecordNick;
        public RelativeLayout rlRecordItem;

        public ViewHolder(View itemView) {
            super(itemView);
            rlRecordItem = itemView.findViewById(R.id.rlRecordItem);
            ivRecord = itemView.findViewById(R.id.ivRecord);
            tvRecordTitle = itemView.findViewById(R.id.tvRecordTitle);
            tvRecordDate = itemView.findViewById(R.id.tvRecordDate);
            tvRecordNick = itemView.findViewById(R.id.tvRecordNick);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.record_grid_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvRecordTitle.setText(recordList.get(position).getRecordTitle());
        holder.tvRecordDate.setText(recordList.get(position).getRecordDate());
        Glide.with(context)
                .load(recordList.get(position).getThumbnailImg())
                .into(holder.ivRecord);
        if (isGroup) {
            holder.tvRecordNick.setText(" · " + recordList.get(position).getNickname());
            holder.tvRecordNick.setVisibility(View.VISIBLE);
        }
        if (isRecordMain) {
            holder.rlRecordItem.setPadding(13, 0, 13, 0);
        }
    }

    public Record getItem(int id) {
        return recordList.get(id);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }
}
