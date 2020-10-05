package ddwucom.mobile.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public MarkerWindowAdapter(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marker_infowindow, null);

        TextView name_tv = view.findViewById(R.id.y_mpn);
        TextView details_tv = view.findViewById(R.id.y_maddr);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        return view;
    }
}
