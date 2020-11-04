package ddwucom.mobile.travel;

import android.content.Context;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    private Context context;
    private int layout;
    private ArrayList<MyReview> myReviewList;
    private LayoutInflater layoutInflater;
    private rvImagePagerAdapter adapter;
    HashMap<Integer, Integer> mViewPagerState = new HashMap<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        PageIndicatorView pageIndicatorView;
        TextView userId;
        RatingBar ratingbar;
        TextView ratingview;
        TextView date;
        TextView content;
        TextView score1;
        TextView score2;
        TextView score3;
        TextView score4;

        public ViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.y_rvImgVP);
            pageIndicatorView = itemView.findViewById(R.id.y_rvImgPic);
            userId = itemView.findViewById(R.id.y_userId);
            ratingbar = itemView.findViewById(R.id.rL_ratingBar);
            ratingview = itemView.findViewById(R.id.rL_rbView);
            date = itemView.findViewById(R.id.y_rLDate);
            content = itemView.findViewById(R.id.y_reviewContent);
            score1 = itemView.findViewById(R.id.cleanliness);
            score2 = itemView.findViewById(R.id.costEffectiveness);
            score3 = itemView.findViewById(R.id.vibe);
            score4 = itemView.findViewById(R.id.accessibility);
        }
    }

    public ReviewListAdapter(Context context, ArrayList<MyReview> myReviewList) {
        this.context = context;
        this.myReviewList = myReviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewlist_adapter_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.userId.setText(myReviewList.get(position).getUserId());
        holder.ratingbar.setRating((float) myReviewList.get(position).getRating());
        holder.ratingview.setText(String.valueOf(myReviewList.get(position).getRating()));
        holder.date.setText(myReviewList.get(position).getDate());
        holder.content.setText(myReviewList.get(position).getContent());
        holder.score1.setText(String.valueOf(myReviewList.get(position).getScore1()));
        holder.score2.setText(String.valueOf(myReviewList.get(position).getScore2()));
        holder.score3.setText(String.valueOf(myReviewList.get(position).getScore3()));
        holder.score4.setText(String.valueOf(myReviewList.get(position).getScore4()));

        adapter = new rvImagePagerAdapter(context, myReviewList.get(position).getReviewImages());
        holder.pageIndicatorView.setCount(myReviewList.get(position).getReviewImages().size());
        holder.viewPager.setAdapter(adapter);
        holder.viewPager.setId(position + 1);
        holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                holder.pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        if (mViewPagerState.containsKey(position)) {
            holder.viewPager.setCurrentItem(mViewPagerState.get(position));
        }
    }

    public void onViewRecycled(ViewHolder holder) {
        mViewPagerState.put(holder.getAdapterPosition(), holder.viewPager.getCurrentItem());
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        if (myReviewList != null)
            return myReviewList.size();
        else
            return 0;
    }

    public class rvImagePagerAdapter extends PagerAdapter {
        private Context context;
        private List<String> images;

        public rvImagePagerAdapter(Context context, List<String> images) {
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
                v = inflater.inflate(R.layout.review_image_for_viewpager, container, false);
            }
            ImageView iv = v.findViewById(R.id.y_rvImgForVp);
            Glide.with(context)
                    .load(images.get(position))
                    .into(iv);
            container.addView(v);
            return v;
        }
    }
}
