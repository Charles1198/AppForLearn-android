package com.bqteam.appforlearn.function.map;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bqteam.appforlearn.R;
import com.bqteam.appforlearn.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author charles
 */
public class GaodeMapActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener,
        GeocodeSearch.OnGeocodeSearchListener{
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
        bankList.add(new Bank("中国银行福州路营业厅", "中国银行福州路支行"));
        bankList.add(new Bank("中国银行九江路营业厅", "中国银行九江路支行"));
        bankList.add(new Bank("中国银行延安东路营业厅", "中国银行延安东路支行"));
        bankList.add(new Bank("中国银行黄埔营业厅", "中国银行黄埔支行"));
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

    @Override
    public void onMyLocationChange(Location location) {
        LatLng myLocaion;
        if (location != null && !(location.getLatitude() == 0 && location.getLongitude() == 0)) {
            myLocaion = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            myLocaion = new LatLng(31.22, 121.48);
        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(myLocaion));

        getPosition();
    }

    private void getPosition() {
        GeocodeSearch gs = new GeocodeSearch(this);
        gs.setOnGeocodeSearchListener(this);
        for (Bank bank : bankList) {
            GeocodeQuery gq = new GeocodeQuery(bank.getAddress(), "上海市");
            gs.getFromLocationNameAsyn(gq);
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        LogUtil.d("中国银行位于：", regeocodeResult.toString());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (i == 1000) {
            for (GeocodeAddress add : geocodeResult.getGeocodeAddressList()) {
                showPosition(add);
            }
        } else {
            Log.d("查询失败", i + "");
        }

    }

    private void showPosition(GeocodeAddress address) {
        String addressName = address.getFormatAddress();
        LatLng latLng = new LatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude());
        aMap.addMarker(new MarkerOptions().position(latLng).title(addressName));

        aMap.setOnMarkerClickListener(marker -> {
            Toast.makeText(GaodeMapActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}
