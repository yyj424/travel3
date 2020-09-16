package ddwucom.mobile.travel;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ReviewImageAdapter extends RecyclerView.Adapter<ReviewImageAdapter.ViewHolder> {

    private ArrayList<Uri> ImageList = null;
    private Context context;

    public ReviewImageAdapter(Context context, ArrayList<Uri> ImageList) {
        this.context = context;
        this.ImageList = ImageList;
    }

    @Override
    public ReviewImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.reviewimage_adapter_view, parent, false);

        return new ReviewImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewImageAdapter.ViewHolder holder, int position) {
        Uri uri = ImageList.get(position);

        holder.Rimage.setImageURI(uri);
        holder.Rimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView Rimage;

        public ViewHolder(View itemView) {
            super(itemView);
            Rimage = itemView.findViewById(R.id.y_reviewImage);
        }
    }
}
