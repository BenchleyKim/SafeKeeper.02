package com.example.safekeeper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.Manifest;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, NavigationView.OnNavigationItemSelectedListener,LocationListener , GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    static FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private GoogleMap mMap;
    private UiSettings mUisettings;
    MarkerOptions markerOptions;
    private ArrayList<SafeMarker> arrayList_safemarker;
    private ArrayList<CctvMarker> arrayList_cctvmarker;

    private Marker currentLocationMarker;
    private LatLng currentlocationLatLng;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private FloatingActionButton fb_openmenu;


    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    Location mCurrentLocation;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout;

    private  DatabaseReference database;

    private DrawerLayout drawerLayout;

    //버튼들의 키 값
    int key_value = 0;
    int state_value = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }




        //프래그먼트 관리자 선언
        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.googlemaps);
        mapFragment.getMapAsync(this);

        //플로팅 버튼 액션
        fb_openmenu = findViewById(R.id.fab_expand_menu_button);

        //위험지역 추가
        FloatingActionButton fb_addanger=findViewById(R.id.fab_adddanger);
        fb_addanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("지도를 눌러 위험지역을 설정해주세요.");
                key_value = 2;
                fb_openmenu.callOnClick();
                
            }
        });
        //귀가파트너 구하기 액션


        //조작 버튼 액션
        FloatingActionButton fb_hand=findViewById(R.id.fab_hand);
        fb_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("지도를 눌러 움직이세요");
                key_value=0;
                fb_openmenu.callOnClick();


            }
        });
        //
        FloatingActionButton fb_safearea = findViewById(R.id.fab_showsafe_area);
        fb_safearea.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(key_value==3){
                    showToast("안전지역을 가립니다.");

                    key_value=1;
                    mMap.clear();
                    onMapReady(mMap);
                }else {
                    key_value = 3;
                    onMapReady(mMap);
                    showToast("안전지역을 보여줍니다.");
                }
                fb_openmenu.callOnClick();
            }
        });
        //현재위치 보내기 버튼
        final FloatingActionButton fb_sendmylocation = findViewById(R.id.fab_sendmylocation);
        fb_sendmylocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(state_value == 0){

                    showToast("현재위치를 전송합니다.");
                    fb_openmenu.callOnClick();
                    fb_sendmylocation.setTitle("현재위치전송 중지");
                    state_value =1;
                }
                else if(state_value ==1){
                    showToast("현재위치를 전송을 중지합니다");
                    fb_openmenu.callOnClick();
                    fb_sendmylocation.setTitle("현재위치 보내기");
                    state_value =0;


                }
            }
        });
        FloatingActionButton fb_search = findViewById(R.id.fab_search);
        fb_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showToast("귀가파트너를 찾았습니다");
                fb_openmenu.callOnClick();
            }
        });



        //네비게이션 메뉴 추가
        Toolbar toolbar = findViewById(R.id.toolbar);//툴바 선언
        setSupportActionBar(toolbar);//툴바연결
        drawerLayout = findViewById(R.id.layout_main);


        NavigationView navigationView = findViewById(R.id.nav_view);//네비게이션 뷰 연결
        navigationView.setNavigationItemSelectedListener(this);//네비게이션 메뉴 아이템 클릭 리스너

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);//토글 선언
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();//토글상태확인




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.googlemaps, new BookmarkFragment()).commit();
                //프로필 기능 추가 프레그먼트의 이름을 실수로 잘못 지음
                break;
            case R.id.nav_bookmark:
                getSupportFragmentManager().beginTransaction().replace(R.id.googlemaps, new ProfileFragment()).commit();
                //북마크 기능 추가 프레그먼트 이름을 실수로 잘못 지음
                break;
            case R.id.nav_map:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                //맵 기능 추가
                break;
            case R.id.nav_report:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report);
                dialog.setTitle("신고하기");
                Button btn_sendreport = (Button)dialog.findViewById(R.id.btn_sendreport);
                Button btn_cancelreport = (Button)dialog.findViewById(R.id.btn_cancelreport);
                final RadioButton rbtn_callpolice = (RadioButton)dialog.findViewById(R.id.rbtn_callpolice);
                final RadioButton rbtn_sendreport = (RadioButton)dialog.findViewById(R.id.rbtn_sendreport);
                btn_sendreport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(rbtn_callpolice.isChecked()){
                            String tel = "tel:112";
                            showToast("경찰에 전화합니다.");
                            startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                            dialog.dismiss();
                        }
                        else if(rbtn_sendreport.isChecked()){
                            getSupportFragmentManager().beginTransaction().replace(R.id.googlemaps, new ReportFragment()).commit();
                            dialog.dismiss();

                        }else{
                            showToast("선택해주세요.");
                        }
                    }
                });
                btn_cancelreport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();



                //리포트 기능 추가
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    public void showToast(String Message){
        Toast.makeText(this,Message,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        //UI세팅 선언
        mUisettings = mMap.getUiSettings();
        //확대 축소 컨트롤러 활성화

        mUisettings.setZoomControlsEnabled(true);


        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        //enableMyLocation();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }






        LatLng location = new LatLng(37.558351, 127.000184); //구글 코리아 위치
        markerOptions = new MarkerOptions();
        markerOptions.title("동국대학교");

        markerOptions.position(location);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16));
        googleMap.setPadding(0,0,0,0);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

                switch (key_value){
                    case 2:
                        final Dialog setDialog = new Dialog(MainActivity.this);
                        setDialog.setContentView(R.layout.custom_menu);
                        setDialog.setTitle("설정창");
                        Button btn_setMarker = (Button)setDialog.findViewById(R.id.btn_setting);
                        Button btn_cancelsetting= (Button)setDialog.findViewById(R.id.btn_cancelsetting);
                        final EditText et_markername = (EditText)setDialog.findViewById(R.id.et_titledangerarea);
                        final EditText et_markerscript = (EditText)setDialog.findViewById(R.id.et_script);

                        final String markertitle = et_markername.getText().toString();
                        final String markerscript = et_markerscript.getText().toString();


                        btn_setMarker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(et_markername.getText().toString().length() != 0 && et_markerscript.getText().toString().length() != 0){
                                   //googleMap.addMarker(markerOptions.position(latLng).title(markertitle).snippet(markerscript));

                                   database = FirebaseDatabase.getInstance().getReference();
                                   SafeMarker safeMarker = new SafeMarker(et_markername.getText().toString(),et_markerscript.getText().toString(),latLng.longitude, latLng.latitude);
                                   database.child("SafeMarker").push().setValue(safeMarker);
                                   setDialog.dismiss();
                                }else{
                                    showToast("입력을 완료해주세요.");
                                }
                            }
                        });
                        btn_cancelsetting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setDialog.dismiss();
                            }
                        });
                        setDialog.show();

                        break;




                }

            }

        });

        database = FirebaseDatabase.getInstance().getReference("SafeMarker");
        arrayList_safemarker = new ArrayList<SafeMarker>();
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SafeMarker safeMarker;
                int mcnt = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("ForClycle", "count" + mcnt++);

                    safeMarker = dataSnapshot.getValue(SafeMarker.class);
                    Log.i("SafeMarker", safeMarker.getClass().toString());


                    arrayList_safemarker.add(safeMarker);
                }
                int i = 0;
                while (arrayList_safemarker.size() > 0) {
                    LatLng latLng_safemarker = new LatLng(arrayList_safemarker.get(0).getMarker_Lat(), arrayList_safemarker.get(0).getMarker_Lng());

                    Log.i("LatLng_safeMarker", latLng_safemarker.toString());
                    googleMap.addMarker(markerOptions.position(latLng_safemarker).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_warning_black_24dp)).title(arrayList_safemarker.get(0).getMarkerTitle()).snippet(arrayList_safemarker.get(0).getMarkerSnippet()));

                    Log.i("markeradd", "action");
                    Log.d("Snapshot", dataSnapshot.getValue().toString());
                    Log.d("Snapshot", arrayList_safemarker.get(0).toString());
                    arrayList_safemarker.remove(0);


                }
                Log.i("markeradd", "action");
                Log.d("Snapshot", dataSnapshot.getValue().toString());
                arrayList_safemarker.clear();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(key_value==3) {
            //SafeMarker데이터 가져오기


            //CCTV데이터 맵뷰에 올리기

            database = FirebaseDatabase.getInstance().getReference("CCTV");
            arrayList_cctvmarker = new ArrayList<CctvMarker>();
            database.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    CctvMarker cctvMarker;
                    int mcnt = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.i("ForClycle", "count" + mcnt++);

                        cctvMarker = dataSnapshot.getValue(CctvMarker.class);
                        Log.i("CCTVMarker", cctvMarker.getClass().toString());


                        arrayList_cctvmarker.add(cctvMarker);
                    }
                    int i = 0;
                    while (arrayList_cctvmarker.size() > 0) {
                        LatLng latLng_cctvmarker = new LatLng(arrayList_cctvmarker.get(0).getCctv_Lat(), arrayList_cctvmarker.get(0).getCctv_Lng());

                        Log.i("LatLng_safeMarker", latLng_cctvmarker.toString());
                        googleMap.addMarker(markerOptions.position(latLng_cctvmarker).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_videocam_black_24dp)).title(arrayList_cctvmarker.get(0).getCctv_title()).snippet("call : " + arrayList_cctvmarker.get(0).getCctv_call() + " \n설치일 : " + arrayList_cctvmarker.get(0).getCctv_settingdate()));

                        Log.i("markeradd", "action");
                        Log.d("Snapshot", dataSnapshot.getValue().toString());
                        Log.d("Snapshot", arrayList_cctvmarker.get(0).toString());
                        arrayList_cctvmarker.remove(0);


                    }
                    Log.i("markeradd", "action");
                    Log.d("Snapshot", dataSnapshot.getValue().toString());
                    arrayList_cctvmarker.clear();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        //mMap.setMyLocationEnabled(true);


    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();

        return false;
    }
}
