package ddwucom.mobile.travel;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;

import java.util.HashMap;
import java.util.List;

public class RecordDayAdapter extends RecyclerView.Adapter<RecordDayAdapter.ViewHolder> {
    private final String TAG = "RecordDayAdapter";

    private List<RecordContent> recordItems;
    private FragmentManager fragmentManager;

    List<String> images;
    HashMap<Integer, Integer> mViewPagerState = new HashMap<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewPager vpRecordImage;
        public TextView tvContent;
        public TextView tvLocation;
        public PageIndicatorView pivRecordImage;

        public ViewHolder(View itemView) {
            super(itemView);
            vpRecordImage = itemView.findViewById(R.id.vpRecordImage);
            tvContent = itemView.findViewById(R.id.tvRecordContent);
            tvLocation = itemView.findViewById(R.id.tvRecordLocation);
            pivRecordImage = itemView.findViewById(R.id.pivRecordImage);
            Log.d("plz", tvContent.toString());
        }
    }

    public RecordDayAdapter(FragmentManager fragmentManager, List<RecordContent> recordItems) {
        this.recordItems = recordItems;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_day_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        Handler delayHandler = new Handler();
//        delayHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // TODO
//                Log.d("plz", "이제 안 기다려");
//            }
//        }, 10000);


        Log.d("plz", holder.toString());
        Log.d("plz", holder.tvContent.toString());
        Log.d("plz", recordItems.get(position).getLocation());

        holder.tvLocation.setText(recordItems.get(position).getLocation());
        holder.tvContent.setText(recordItems.get(position).getContent());
        RecordPagerAdapter recordPagerAdapter = new RecordPagerAdapter(fragmentManager);
        images = recordItems.get(position).getImages();
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
        Log.d("goeun", recordItems.get(position).getContent());
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

    public class RecordPagerAdapter extends FragmentPagerAdapter {
        public RecordPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return RecordImageFragment.newInstance(images.get(position));
        }

        @Override
        public int getCount() {
            if (images != null) {
                return images.size();
            }
            else
                return 0;
        }
    }
}
