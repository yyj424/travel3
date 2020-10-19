package ddwucom.mobile.travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class RecordImageFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public RecordImageFragment() {

    }

    public static RecordImageFragment newInstance(String param1) {
        RecordImageFragment fragment = new RecordImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.record_image_view, container, false);
        ImageView iv = v.findViewById(R.id.ivRecordDayImage);
        Glide.with(this)
                .load(mParam1)
                .into(iv);
        return v;
    }
}
