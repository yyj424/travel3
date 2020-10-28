package ddwucom.mobile.travel;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AddRecordImageAdapter extends RecyclerView.Adapter<AddRecordImageAdapter.ViewHolder> {

    private ArrayList<Uri> imageList = null;
    private Context context;

    public AddRecordImageAdapter(Context context, ArrayList<Uri> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public AddRecordImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.add_record_image_adapter_view, parent, false);

        return new AddRecordImageAdapter.ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(AddRecordImageAdapter.ViewHolder holder, final int position) {
        Uri uri = imageList.get(position);

        holder.imageView.setImageURI(uri);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        holder.imageRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, imageList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public GradientDrawable drawable;
        ImageButton imageRemoveBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivRecord);
            drawable = (GradientDrawable) context.getDrawable(R.drawable.background_rounding);
            imageView.setBackground(drawable);
            imageView.setClipToOutline(true);
            imageRemoveBtn = itemView.findViewById(R.id.btn_image_remove);
        }
    }
}
