package ddwucom.mobile.travel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class HomeReviewAdapter extends RecyclerView.Adapter<HomeReviewAdapter.ViewHolder> {
private Context context;
private ArrayList<MyReview> myReviewList;

public static class ViewHolder extends RecyclerView.ViewHolder {
    RatingBar ratingbar;
    TextView ratingview;
    TextView pname;
    ImageView img;

    public ViewHolder(View itemView) {
        super(itemView);
        pname = itemView.findViewById(R.id.y_home_review_pname);
        ratingbar = itemView.findViewById(R.id.y_home_review_rating);
        ratingview = itemView.findViewById(R.id.y_home_review_ratingView);
        img = itemView.findViewById(R.id.y_home_review_img);
    }
}

    public HomeReviewAdapter(Context context, ArrayList<MyReview> myReviewList) {
        this.context = context;
        this.myReviewList = myReviewList;
    }

    @Override
    public HomeReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_reviews_card_view, parent, false);
        HomeReviewAdapter.ViewHolder viewHolder = new HomeReviewAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeReviewAdapter.ViewHolder holder, int position) {
        holder.pname.setText(myReviewList.get(position).getPname());
        holder.ratingbar.setRating((float) myReviewList.get(position).getRating());
        holder.ratingview.setText(String.valueOf(myReviewList.get(position).getRating()));
        Glide.with(context)
                .load(myReviewList.get(position).getImg())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        if (myReviewList != null)
            return myReviewList.size();
        else
            return 0;
    }
}
