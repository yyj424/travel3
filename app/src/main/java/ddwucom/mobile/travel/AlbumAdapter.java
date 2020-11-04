package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private static final String TAG = "AlbumAdapter";

    Context context;
    ArrayList<String> imageList;

    public AlbumAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.album_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(imageList.get(position))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivAlbumItem);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivAlbumItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ivAlbumItem = itemView.findViewById(R.id.ivAlbumItem);
        }
    }
}
