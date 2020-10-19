package ddwucom.mobile.travel;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ReviewListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<MyReview> myReviewList;
    private LayoutInflater layoutInflater;
    private rvImagePagerAdapter adapter;

    public ReviewListAdapter(Context context, int layout, ArrayList<MyReview> myReviewList) {
        this.context = context;
        this.layout = layout;
        this.myReviewList = myReviewList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myReviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return myReviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(myReviewList.get(position).getUserId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);
        }

        ViewPager viewPager = convertView.findViewById(R.id.y_rvImgVP);
        final PageIndicatorView pageIndicatorView = convertView.findViewById(R.id.y_rvImgPic);
        TextView userId = convertView.findViewById(R.id.y_userId);
        RatingBar ratingbar = convertView.findViewById(R.id.rL_ratingBar);
        TextView ratingview = convertView.findViewById(R.id.rL_rbView);
        TextView date = convertView.findViewById(R.id.y_rLDate);
        TextView content = convertView.findViewById(R.id.y_reviewContent);
        TextView score1 = convertView.findViewById(R.id.cleanliness);
        TextView score2 = convertView.findViewById(R.id.costEffectiveness);
        TextView score3 = convertView.findViewById(R.id.vibe);
        TextView score4 = convertView.findViewById(R.id.accessibility);

        adapter = new rvImagePagerAdapter(context, myReviewList.get(pos).getReviewImages());
        viewPager.setAdapter(adapter);
        pageIndicatorView.setCount(myReviewList.get(pos).getReviewImages().size());
        viewPager.setId(position + 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        userId.setText(myReviewList.get(pos).getUserId());
        ratingbar.setRating((float) myReviewList.get(pos).getRating());
        ratingview.setText(String.valueOf(myReviewList.get(pos).getRating()));
        date.setText(myReviewList.get(pos).getDate());
        content.setText(myReviewList.get(pos).getContent());
        score1.setText(String.valueOf(myReviewList.get(pos).getScore1()));
        score2.setText(String.valueOf(myReviewList.get(pos).getScore2()));
        score3.setText(String.valueOf(myReviewList.get(pos).getScore3()));
        score4.setText(String.valueOf(myReviewList.get(pos).getScore4()));

        return convertView;
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
