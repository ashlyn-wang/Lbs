package com.example.lbs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.amap.api.services.share.ShareSearch;
import com.amap.api.services.share.ShareSearch.OnShareSearchListener;
import com.amap.api.services.share.ShareSearch.ShareFromAndTo;
import com.amap.api.services.share.ShareSearch.ShareNaviQuery;

import android.R.bool;
import android.R.color;
import android.R.drawable;
import android.R.integer;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ZoomButton;
import android.widget.ZoomControls;

public class MainActivity extends Activity implements OnClickListener,
		LocationSource, AMapLocationListener
{

	private MapView mapView;
	private AMap aMap;

	private Button normal_but,sate_but,night_but,place_but,circle_but,line_but;
	private ImageButton share_but,road_but,traffic_but,search_but,navi_but,location_but,soundauto_but;
	private EditText editText;

	boolean traffic_flag = true;
	boolean navi_flag = false;
	boolean olddriver_flag = false;
	boolean soundauto_flag = false;

	UiSettings mUiSettings;
	MarkerDo markerDo;
	double location_s;
	double location_e;
	FindRoad findRoad = new FindRoad();
	OldDriver oldDriver = new OldDriver();
	
	boolean flag_auto = false;
	  IntentFilter fliter ;
	private PendingIntent mPendingIntent = null;
	public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);  
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		initMap();
		initUI();
	    
		//Log.i("tag", "ZoomPosition = " + aMap.getUiSettings().getZoomPosition());
	}

	public void initMap()
	{
		// TODO Auto-generated method stub
		
		aMap = mapView.getMap();
		aMap.setLocationSource(this);
		//�������Ű�ť��λ��
		 aMap.getUiSettings().setZoomPosition(1);
		 
		 aMap.setLocationSource(this);//���ö�λ����
		 //aMap.getUiSettings().setMyLocationButtonEnabled(true);// �Ƿ���ʾ��λ��ť
		 aMap.setMyLocationEnabled(true);// �Ƿ�ɴ�����λ����ʾ��λ��
	     aMap.getUiSettings().setCompassEnabled(true);  //ָ����ɼ�
	     markerDo = new MarkerDo();
	     markerDo.doMarker(aMap, this);
	     aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	     
	     
	     //����Χ��
	    fliter = new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION);
			fliter.addAction(GEOFENCE_BROADCAST_ACTION);
			
	     Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION);
	     mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
					intent, 0);
	     
	     
	    
	}

	private void initUI()
	{
		
		// TODO Auto-generated method stub
		normal_but = (Button) findViewById(R.id.normal);
		sate_but = (Button) findViewById(R.id.sate);
		night_but = (Button) findViewById(R.id.night);
		share_but = (ImageButton) findViewById(R.id.share);
		road_but = (ImageButton) findViewById(R.id.road);
		traffic_but = (ImageButton) findViewById(R.id.traffic);
		search_but = (ImageButton) findViewById(R.id.search);
		navi_but = (ImageButton) findViewById(R.id.navi);
		location_but = (ImageButton) findViewById(R.id.location);
		soundauto_but = (ImageButton) findViewById(R.id.sound);
		editText = (EditText) findViewById(R.id.edittext);
		
		
		normal_but.setBackgroundColor(getResources().getColor(R.color.normal));
		sate_but.setBackgroundColor(getResources().getColor(R.color.selt));
		night_but.setBackgroundColor(getResources().getColor(R.color.night));
		
		//���ð�ť͸����
		normal_but.getBackground().setAlpha(100);
		sate_but.getBackground().setAlpha(100);
		night_but.getBackground().setAlpha(100);
		share_but.getBackground().setAlpha(150);
		road_but.getBackground().setAlpha(150);
		navi_but.getBackground().setAlpha(150);		
		traffic_but.getBackground().setAlpha(150);
		search_but.getBackground().setAlpha(150);
		location_but.getBackground().setAlpha(150);
		soundauto_but.getBackground().setAlpha(150);
		

		normal_but.setOnClickListener(this);
		sate_but.setOnClickListener(this);
		night_but.setOnClickListener(this);
		traffic_but.setOnClickListener(this);
		navi_but.setOnClickListener(this);
		search_but.setOnClickListener(this);
		location_but.setOnClickListener(this);
		road_but.setOnClickListener(this);
		share_but.setOnClickListener(this);
		soundauto_but.setOnClickListener(this);
		
	
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.normal:
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);
			break;
		case R.id.sate:
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.night:
			aMap.setMapType(AMap.MAP_TYPE_NIGHT);
			break;
		case R.id.traffic:
			if (traffic_flag == false)
			{
				aMap.setTrafficEnabled(false);
				traffic_flag = true;
			} else
			{
				aMap.setTrafficEnabled(true);
				traffic_flag = false;
			}
			break;
			
		case R.id.location:
			 Toast.makeText(MainActivity.this, "��λ", Toast.LENGTH_SHORT).show();
			 aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
			 aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			break;
			
		case R.id.navi:
			if (navi_flag == false)
			{	
				if (markerDo.getMarkPosition() == null)
				{
					Toast.makeText(MainActivity.this, "��ѡ����Ҫȥ�ĵط���", Toast.LENGTH_SHORT).show();
				}else {
					findRoad.setPosition(markerDo.getMarkPosition(),location_s,location_e);
					findRoad.doFindRoad(this, aMap);
					aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
					navi_flag = true;
				}
				
				
			}else {
				aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
				aMap.clear();
				initMap();
				navi_flag = false;
			}
			
			
			break;
		case R.id.search:
			Search search = new Search();
			search.doSearchQuery(editText,this,aMap);
			break;
			
		case R.id.road:
			if (navi_flag == false)
			{
				oldDriver.doTripRoad(this, aMap);
				navi_flag = true;
			}else {
				aMap.clear();
				initMap();
				navi_flag = false;
			}
			
			break;
		case R.id.sound:
			if (soundauto_flag == false)
			{
				Toast.makeText(MainActivity.this, "�Զ���������ģʽ", Toast.LENGTH_SHORT).show();
				soundauto_but.setImageResource(R.drawable.sound);
				registerReceiver(mGeoFenceReceiver, fliter);
				
				soundauto_flag = true;
			}else {
				Toast.makeText(MainActivity.this, "�ر��Զ�����ģʽ", Toast.LENGTH_SHORT).show();
				soundauto_but.setImageResource(R.drawable.nosound);
				unregisterReceiver(mGeoFenceReceiver);
				soundauto_flag = false;
				Intent intent2 = new Intent(this,VideoService.class);
				 intent2.putExtra("playing", true);
				 this.stopService(intent2);
			}
			break;
		
		case R.id.share:
			
			if (markerDo.getMarkPosition() == null)
			{
				Toast.makeText(this, "����ѡ����ĵ���·���ٽ��з���", Toast.LENGTH_SHORT);
			}else {
				Share share = new Share();
				share.setPosition(this, aMap, markerDo.getMarkPosition(), location_s, location_e);
				//share.shareNavi();
				share.showShare();
			}
			
			break;

		}
	}

	@Override
	public void activate(OnLocationChangedListener arg0)
	{
		// TODO Auto-generated method stub
		mListener = arg0;
		if (mlocationClient == null)
		{
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// ���ö�λ����
			mlocationClient.setLocationListener(this);
			//���ö�λ���,��λ����,Ĭ��Ϊ2000ms
			mLocationOption.setInterval(2000);
			// ����Ϊ�߾��ȶ�λģʽ
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// ���ö�λ����
			mlocationClient.setLocationOption(mLocationOption);
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����stopLocation()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���onDestroy()����
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������stopLocation()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
		mlocationClient.startLocation();
		}
		
	}
	
	

	@Override
	public void deactivate()
	{
		// TODO Auto-generated method stub 
		mListener = null;
		if (mlocationClient != null)
		{
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation)
	{
		// TODO Auto-generated method stub
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				//mLocationErrText.setVisibility(View.GONE);
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
				location_s = amapLocation.getLatitude();
				location_e = amapLocation.getLongitude();
				
				mlocationClient.addGeoFenceAlert("apollo", 32.10777867, 118.93012404, 1000, -1, mPendingIntent);
			} else {
				String errText = "��λʧ��," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
				//mLocationErrText.setVisibility(View.VISIBLE);
				//mLocationErrText.setText(errText);
			}
		}
		 
	}
	
	
		BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// ���չ㲥
				if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
					Bundle bundle = intent.getExtras();
					// ���ݹ㲥��event��ȷ�����������ڻ�����������
					int status = bundle.getInt("event");
					String geoFenceId = bundle.getString("fenceid");
					if (status == 1) {
						// ����Χ������
						// �����Զ������ѷ�ʽ,ʾ����ʹ�õ������ַ�ʽ
						Toast.makeText(context, "����Χ������", Toast.LENGTH_SHORT).show();
						
							Intent intent2 = new Intent(context,VideoService.class);
							intent2.putExtra("place", "apollo");
							intent2.putExtra("playing", true);
							context.startService(intent2);
					} else if (status == 2) {
						// �뿪Χ������
						// �����Զ������ѷ�ʽ,ʾ����ʹ�õ������ַ�ʽ
						//Toast.makeText(context, "�뿪Χ������", Toast.LENGTH_SHORT).show();
						Intent intent2 = new Intent(context,VideoService.class);
						 intent2.putExtra("playing", true);
						 context.stopService(intent2);
					}
				}
			}

			
		};

	
	
		
	
	
}
