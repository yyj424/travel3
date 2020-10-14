package ddwucom.mobile.travel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<MyReview> myReviewList;
    private LayoutInflater layoutInflater;

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

        TextView userId = convertView.findViewById(R.id.y_userId);
        RatingBar ratingbar = convertView.findViewById(R.id.rL_ratingBar);
        TextView ratingview = convertView.findViewById(R.id.rL_rbView);
        TextView date = convertView.findViewById(R.id.y_rLDate);
        TextView content = convertView.findViewById(R.id.y_reviewContent);
        TextView score1 = convertView.findViewById(R.id.cleanliness);
        TextView score2 = convertView.findViewById(R.id.costEffectiveness);
        TextView score3 = convertView.findViewById(R.id.vibe);
        TextView score4 = convertView.findViewById(R.id.accessibility);

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
}
