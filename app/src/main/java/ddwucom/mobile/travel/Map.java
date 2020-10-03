package ddwucom.mobile.travel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Map extends AppCompatActivity {

    String [] permission_list ={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private Marker currentMarker = null;
    LocationManager locationManager;
    GoogleMap map;
    private RecyclerView listview;
    private ArrayList<MyCourse> courseList = null;
    private CourseListAdapter courseListAdapter;
    LatLng position;
    double latitude = 100.0;
    double longitude = 200.0;
    Location location1;
    Location location2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission_list, 0);
        } else {
            init();
        }
        courseList();//Log.d("yc","함수끝");
        //etsearch = findViewById(R.id.y_searchPlace);

        String apiKey = getResources().getString(R.string.google_maps_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
 //       PlacesClient placesClient = Places.createClient(Map.this);

       // autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
        
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.y_searchPlace);
//
//        //autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NotNull Place place) {
//                // Get info about the selected place.
//                Log.d("yj", "Place: " + place.getName() + ", " + place.getId());
//            }
//
//            @Override
//            public void onError(@NotNull Status status) {
//                // Handle the error.
//                Log.d("yj", "An error occurred: " + status);
//            }
//        });
//        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch (actionId) {
//                    case EditorInfo.IME_ACTION_SEARCH:
//                        findPlace();
//                        break;
//                    default:
//                        etsearch.clearFocus();
//                        imm.hideSoftInputFromWindow(etsearch.getWindowToken(), 0);
//                        return false;
//                }
//                return true;
//            }
//        });
    }



    private void courseList() {
        listview = findViewById(R.id.y_course_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);
        courseList = new ArrayList();
        courseList.add(new MyCourse(1, "포레스트\n아웃팅스"));
        courseList.add(new MyCourse(2, "일산칼국수"));
        courseList.add(new MyCourse(3, "스타필드"));
        courseList.add(new MyCourse(1, "포레스트\n아웃팅스"));
        courseList.add(new MyCourse(2, "일산칼국수"));
        courseList.add(new MyCourse(3, "스타필드"));
        courseList.add(new MyCourse(1, "포레스트\n아웃팅스"));
        courseList.add(new MyCourse(2, "일산칼국수"));
        courseList.add(new MyCourse(3, "스타필드"));
        courseList.add(new MyCourse(1, "포레스트\n아웃팅스"));
        courseList.add(new MyCourse(2, "일산칼국수"));
        courseList.add(new MyCourse(3, "스타필드"));
        courseListAdapter = new CourseListAdapter(this, courseList, onClickItem);
        listview.setAdapter(courseListAdapter);
        //!!!!!!!!!!!!!!!!수정!!!!!!!!!!!!!!!!
    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            Toast.makeText(Map.this, str, Toast.LENGTH_SHORT).show();
        }//수정해야됨
    };

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_placeSearch:
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(Map.this);
                startActivityForResult(intent, 100);
            case R.id.y_courseRemove:
                //Intent intent = new Intent(this, );
                //startActivity(intent);
                break;
            case R.id.y_courseRegister:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            longitude = place.getLatLng().longitude;
            latitude = place.getLatLng().latitude;
            getMyLocation();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(Map.this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void findPlace(String address) {
        final Geocoder geocoder = new Geocoder( getApplicationContext() );
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName( address,  20 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null) {
            if (list.size() == 0) {
                Toast.makeText( getApplicationContext(), "해당되는 주소 정보를 찾지 못했습니다.", Toast.LENGTH_SHORT ).show();
            }
            else {
                Address addr = list.get( 0 );
                latitude = addr.getLatitude();
                longitude = addr.getLongitude();
                getMyLocation();
            }
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
        SupportMapFragment mapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);

        MapReadyCallback callback1 = new MapReadyCallback();
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
            setMyLocation(location1);Log.d("yj","loca1");
        }
        else {
            if(location2 != null) {
                setMyLocation(location2);Log.d("yj","loca2");
            }
        }
        //새로운 측정 값
        GetMyLocationListener listener = new GetMyLocationListener();

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
            position = new LatLng(location.getLatitude(), location.getLongitude());Log.d("yj","첫실행");
        }
       else {
            position = new LatLng(latitude, longitude);Log.d("yj","찾은");
        }

        setMarker(position);

        CameraUpdate update1 = CameraUpdateFactory.newLatLng(position);Log.d("yj","1");
        CameraUpdate update2 = CameraUpdateFactory.zoomTo(15f);Log.d("yj","2");

        map.moveCamera(update1);Log.d("yj","업");
        map.animateCamera(update2);Log.d("yj","업2");

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
        markerOptions.title("markerTitle");
        markerOptions.snippet("markerSnippet");
        markerOptions.draggable(true);
        currentMarker = map.addMarker(markerOptions);
    }

    //현재 위치 측정이 성공하면 반응하는 리스너
    class GetMyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //setMyLocation(location); Log.d("yj","getM어쩌구 리스너");
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
