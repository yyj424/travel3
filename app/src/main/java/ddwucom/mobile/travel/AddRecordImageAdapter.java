package ddwucom.mobile.travel;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AddRecordImageAdapter extends RecyclerView.Adapter<AddRecordImageAdapter.ViewHolder> {

    private ArrayList<Uri> ImageList = null;
    private Context context;

    public AddRecordImageAdapter(Context context, ArrayList<Uri> ImageList) {
        this.context = context;
        this.ImageList = ImageList;
    }

    @Override
    public AddRecordImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.add_record_image_adapter_view, parent, false);

        return new AddRecordImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddRecordImageAdapter.ViewHolder holder, int position) {
        Uri uri = ImageList.get(position);

        holder.imageView.setImageURI(uri);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public GradientDrawable drawable;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_record);
            drawable = (GradientDrawable) context.getDrawable(R.drawable.background_rounding);
            imageView.setBackground(drawable);
            imageView.setClipToOutline(true);
        }
    }
}
