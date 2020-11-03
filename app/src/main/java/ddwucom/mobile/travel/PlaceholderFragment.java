package ddwucom.mobile.travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class PlaceholderFragment extends Fragment {
    private static final String TAG = "PlaceholderFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_IMG_URL = "image_url";

    int pos;
    String url;

    public PlaceholderFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_image, container, false);

        final ImageView ivDetailImage = rootView.findViewById(R.id.detail_image);
        Glide.with(getActivity()).load(url).thumbnail(0.1f).into(ivDetailImage);

        return rootView;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        this.pos = args.getInt(ARG_SECTION_NUMBER);
        this.url = args.getString(ARG_IMG_URL);
    }

    public static PlaceholderFragment newInstance(int sectionNumber, String url) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_IMG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }
}
