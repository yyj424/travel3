package ddwucom.mobile.travel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.rd.PageIndicatorView;

import java.util.HashMap;
import java.util.List;

public class RecordDayAdapter extends RecyclerView.Adapter<RecordDayAdapter.ViewHolder> {
    private final String TAG = "RecordDayAdapter";

    private Context context;
    private List<RecordContent> recordItems;
    private boolean isGroup;

    List<String> images;
    HashMap<Integer, Integer> mViewPagerState = new HashMap<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewPager vpRecordImage;
        public TextView tvContent;
        public TextView tvLocation;
        public PageIndicatorView pivRecordImage;
        public TextView tvRecordNickname;

        public ViewHolder(View itemView) {
            super(itemView);
            vpRecordImage = itemView.findViewById(R.id.vpRecordImage);
            tvContent = itemView.findViewById(R.id.tvRecordContent);
            tvLocation = itemView.findViewById(R.id.tvRecordLocation);
            pivRecordImage = itemView.findViewById(R.id.pivRecordImage);
            tvRecordNickname = itemView.findViewById(R.id.tvRecordNickname);
        }
    }

    public RecordDayAdapter(Context context, boolean isGroup, List<RecordContent> recordItems) {
        this.context = context;
        this.isGroup = isGroup;
        this.recordItems = recordItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_day_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (recordItems.get(position).getLocation() != null) {
            holder.tvLocation.setText(recordItems.get(position).getLocation());
        }

        if (isGroup) {
            holder.tvRecordNickname.setText(recordItems.get(position).getNickname());
            holder.tvRecordNickname.setVisibility(View.VISIBLE);
        }

        holder.tvContent.setText(recordItems.get(position).getContent());
        holder.tvContent.setMovementMethod(new ScrollingMovementMethod());

        images = recordItems.get(position).getImages();
        RecordPagerAdapter recordPagerAdapter = new RecordPagerAdapter(context, images);
        holder.pivRecordImage.setCount(images.size());
        holder.vpRecordImage.setAdapter(recordPagerAdapter);
        holder.vpRecordImage.setId(position + 1);
        holder.vpRecordImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                holder.pivRecordImage.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        if (mViewPagerState.containsKey(position)) {
            holder.vpRecordImage.setCurrentItem(mViewPagerState.get(position));
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        mViewPagerState.put(holder.getAdapterPosition(), holder.vpRecordImage.getCurrentItem());
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        if (recordItems != null)
            return recordItems.size();
        else
            return 0;
    }

    public class RecordPagerAdapter extends PagerAdapter {
        private Context context;
        private List<String> images;

        public RecordPagerAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = null;
            if (context != null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.record_image_view, container, false);
            }
            ImageView iv = v.findViewById(R.id.ivRecordDayImage);
            Glide.with(context)
                    .load(images.get(position))
                    .into(iv);

            container.addView(v);
            return v;
        }
    }
}
