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
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;


public class ReviewImageAdapter extends RecyclerView.Adapter<ReviewImageAdapter.ViewHolder> {

    private ArrayList<Uri> ImageList = null;
    private Context context;

    public ReviewImageAdapter(Context context, ArrayList<Uri> ImageList) {
        this.context = context;
        this.ImageList = ImageList;
    }

    @Override
    public ReviewImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.reviewimage_adapter_view, parent, false);

        return new ReviewImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewImageAdapter.ViewHolder holder, final int position) {
        Uri uri = ImageList.get(position);

        holder.Rimage.setImageURI(uri);
        holder.Rimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, ImageList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView Rimage;
        public ImageView remove;
        public GradientDrawable drawable;

        public ViewHolder(View itemView) {
            super(itemView);
            Rimage = itemView.findViewById(R.id.y_reviewImage);
            remove = itemView.findViewById(R.id.y_remove_reviewImage);
            drawable = (GradientDrawable) context.getDrawable(R.drawable.background_rounding);
            Rimage.setBackground(drawable);
            Rimage.setClipToOutline(true);
        }
    }
}
