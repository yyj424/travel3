package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GroupAlbumAdapter extends RecyclerView.Adapter<GroupAlbumAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Album> albumList;
    private OnItemClickListener albumClick = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener albumClick) {
        this.albumClick = albumClick;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView albumName;
        TextView albumSize;
        ImageView albumImg;

        public ViewHolder(View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.y_group_album_folderName);
            albumSize = itemView.findViewById(R.id.y_group_album_folderSize);
            albumImg = itemView.findViewById(R.id.y_group_albumImg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(albumClick != null) {
                            albumClick.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }

    public GroupAlbumAdapter(Context context, ArrayList<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @Override
    public GroupAlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_group_album_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        GroupAlbumAdapter.ViewHolder viewHolder = new GroupAlbumAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupAlbumAdapter.ViewHolder holder, int position) {
        holder.albumName.setText(albumList.get(position).getAlbumName());
        holder.albumSize.setText(albumList.get(position).getImageCnt());
        Glide.with(context)
                .load(albumList.get(position).getThumbnail())
                .into(holder.albumImg);
    }

    @Override
    public int getItemCount() {
        if (albumList != null)
            return albumList.size();
        else
            return 0;
    }
}
