package ddwucom.mobile.travel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class OnlyMap extends AppCompatActivity {

    String [] permission_list ={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    ImageView btnHome, btnGroup, btnCourse, btnMap;
    Marker currentMarker = null;
    LocationManager locationManager;
    GoogleMap map;
    LatLng position;
    double latitude = 100.0;
    double longitude = 200.0;
    String placeName = "현재 위치";
    String address;
    Location location1;
    Location location2;
    String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map);

        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission_list, 0);
        } else {
            init();
        }
        String apiKey = getResources().getString(R.string.google_maps_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_home_place_search:
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ID);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(OnlyMap.this);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_home:
                btnHome.setImageResource(R.drawable.home_icon_yellow);
                btnGroup.setImageResource(R.drawable.friends_icon_grey);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                Intent home = new Intent(OnlyMap.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.btn_friends:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_yellow);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                Intent group = new Intent(OnlyMap.this, GroupMainActivity.class);
                startActivity(group);
                break;
            case R.id.btn_map:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_grey);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_yellow);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            pid = place.getId();
            longitude = place.getLatLng().longitude;
            latitude = place.getLatLng().latitude;
            placeName = place.getName();
            address = place.getAddress();
            getMyLocation();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(OnlyMap.this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }
        init();
    }

    public void init() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.y_home_map);

        OnlyMap.MapReadyCallback callback1 = new OnlyMap.MapReadyCallback();
        mapFragment.getMapAsync(callback1);
    }

    //Google 지도 사용 준비 완료시 동작하는 콜백
    class MapReadyCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            getMyLocation();
        }
    }

    //현재위치 측정 메소드
    public void getMyLocation() {
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        //권한 확인 작업(일부 코드가 권한 작업을 해야 오류 안남)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }

        //이전 측정 값
        location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //이전 측정 값이 있다면
        if(location1 != null) {
            setMyLocation(location1);
        }
        else {
            if(location2 != null) {
                setMyLocation(location2);
            }
        }
        //새로운 측정 값
        OnlyMap.GetMyLocationListener listener = new OnlyMap.GetMyLocationListener();

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, (android.location.LocationListener) listener);
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10f, (android.location.LocationListener) listener);
        }
    }

    public void setMyLocation(Location location) {
        //위도와 경도값을 관리하는 객체
        if (latitude == 100.0 && longitude == 200.0) {
            position = new LatLng(location.getLatitude(), location.getLongitude());
        }
        else {
            position = new LatLng(latitude, longitude);
        }

        setMarker(position);

        //권한 확인 작업
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }
        //현재 위치 표시
        map.setMyLocationEnabled(true);
    }

    public void setMarker(LatLng position) {
        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title(placeName);
        markerOptions.snippet(address);
        markerOptions.draggable(true);
        currentMarker = map.addMarker(markerOptions);

        MarkerWindowAdapter customInfoWindow = new MarkerWindowAdapter(OnlyMap.this);
        map.setInfoWindowAdapter(customInfoWindow);
        currentMarker.showInfoWindow();//되는지 확인

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(OnlyMap.this, ReviewList.class);
                intent.putExtra("placeId", pid);
                intent.putExtra("placeName", placeName);
                intent.putExtra("onlyMap",2);
                startActivity(intent);
            }
        });

        CameraUpdate update1 = CameraUpdateFactory.newLatLngZoom(position, 15);
        map.moveCamera(update1);
    }

    //현재 위치 측정이 성공하면 반응하는 리스너
    class GetMyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            setMyLocation(location);
            locationManager.removeUpdates((android.location.LocationListener) this);//위치 측정 중단
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //상태 변경 시 호출
        }

        @Override
        public void onProviderEnabled(String provider) {
            //위치 측정하기 위한 요소가 사용 불가능시 호출
        }

        @Override
        public void onProviderDisabled(String provider) {
            //위치 측정하기 위한 요소가 사용 가능시 호출
        }
    }
}
