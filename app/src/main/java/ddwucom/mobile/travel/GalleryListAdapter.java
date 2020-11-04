package ddwucom.mobile.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class GalleryListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Album> albumList;
    LayoutInflater layoutInflater;

    public GalleryListAdapter(Context context, ArrayList<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gallery_item, parent, false);

            holder = new ViewHolder();
            holder.ivGalleryThumbnail = convertView.findViewById(R.id.ivGalleryThumbnail);
            holder.tvAlbumName = convertView.findViewById(R.id.tvAlbumName);
            holder.tvImageCnt = convertView.findViewById(R.id.tvImageCnt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (albumList.get(position).getThumbnail() != null) {
            Glide.with(context)
                    .load(albumList.get(position).getThumbnail())
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivGalleryThumbnail);
        }
        holder.ivGalleryThumbnail.setBackground(context.getDrawable(R.drawable.background_rounding));
        holder.ivGalleryThumbnail.setClipToOutline(true);
        holder.tvAlbumName.setText(albumList.get(position).getAlbumName());
        holder.tvImageCnt.setText(String.valueOf(albumList.get(position).getImageCnt()));

        return convertView;
    }

    static class ViewHolder {
        ImageView ivGalleryThumbnail;
        TextView tvAlbumName;
        TextView tvImageCnt;
    }
}
