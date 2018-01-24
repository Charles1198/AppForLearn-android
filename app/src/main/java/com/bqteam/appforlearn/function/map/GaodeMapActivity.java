package com.bqteam.appforlearn.function.map;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bqteam.appforlearn.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author charles
 */
public class GaodeMapActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener {
    @BindView(R.id.mapView)
    MapView mapView;

    private AMap aMap;


    /**
     * 各个网点坐标
     */
    List<Bank> bankList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaode_map);
        ButterKnife.bind(this);

        initData();

        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        showMyLocation();
    }

    private void initData() {
        bankList.add(new Bank("徐汇营业厅", "上海市徐汇区", 31.20,121.48));
        bankList.add(new Bank("奉贤营业厅", "上海市奉贤区", 31.21,121.47));
        bankList.add(new Bank("浦东营业厅", "上海市浦东区", 31.22,121.48));
        bankList.add(new Bank("闵行营业厅", "上海市闵行区", 31.23,121.49));
        bankList.add(new Bank("虹口营业厅", "上海市虹口区", 31.24,121.48));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    private void showMyLocation() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
    }

    private void showPosition() {
        for (int i = 0; i < bankList.size(); i++) {
            aMap.addMarker(new MarkerOptions().position(bankList.get(i).getLatLng()).title(bankList.get(i).getName()));
        }

        aMap.setOnMarkerClickListener(marker -> {
            Toast.makeText(GaodeMapActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location == null) {
            return;
        }
        if (location.getLatitude() == 0 && location.getLongitude() == 0) {
            //定位失败
            aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(31.22, 121.48)));
        } else {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(),
                    location.getLongitude())));
        }

        showPosition();
    }
}
