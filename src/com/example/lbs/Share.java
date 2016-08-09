package com.example.lbs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.share.ShareSearch;
import com.amap.api.services.share.ShareSearch.OnShareSearchListener;
import com.amap.api.services.share.ShareSearch.ShareFromAndTo;
import com.amap.api.services.share.ShareSearch.ShareNaviQuery;

public class Share implements OnShareSearchListener
{ 
	private ShareSearch mShareSearch;
	private LatLonPoint START ;
	private LatLonPoint END ;
	private Context context;
	private AMap mAMap;
	private String url;
	
	public void setPosition(Context context,AMap aMap ,LatLng endlatLng , double location_s ,double location_e)
	{
		START = new LatLonPoint(location_s, location_e);
		END = new LatLonPoint(endlatLng.latitude, endlatLng.longitude);
		this.context = context;
		this.mAMap = aMap;
	}

	@Override
	public void onBusRouteShareUrlSearched(String arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDrivingRouteShareUrlSearched(String arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationShareUrlSearched(String arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNaviShareUrlSearched(String url, int errorCode)
	{
		// TODO Auto-generated method stub
		if (errorCode == 1000) {
			shareUrl(url, "��������");
		} else {
			Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();

		//	ToastUtil.showerror(this.getApplicationContext(), errorCode);
		}
	}

	@Override
	public void onPoiShareUrlSearched(String arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWalkRouteShareUrlSearched(String url, int errorCode)
	{
		// TODO Auto-generated method stub
		if (errorCode == 1000) {
			shareUrl(url, "����·���滮����");
		} else {
			Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
			//ToastUtil.showerror(this.getApplicationContext(), errorCode);
		}
	}
	
	/**
	 * �����̴�����
	 */
	public void shareNavi() {

		mShareSearch = new ShareSearch(context);
		mShareSearch.setOnShareSearchListener(this);
		addRouteMarker();
		ShareFromAndTo fromAndTo = new ShareFromAndTo(START, END);
		ShareNaviQuery query = new ShareNaviQuery(fromAndTo,
				ShareSearch.NaviDefault);
	//	showProgressDialog();
		mShareSearch.searchNaviShareUrlAsyn(query);
	}
	
	/**
	 * �����·marker
	 */
	private void addRouteMarker() {
		mAMap.clear();
		addMarker(START.getLatitude(), START.getLongitude(), "", "",
				BitmapDescriptorFactory.fromResource(R.drawable.amap_start));
		addMarker(END.getLatitude(), END.getLongitude(), "", "",
				BitmapDescriptorFactory.fromResource(R.drawable.amap_end));
		LatLngBounds.Builder builder = LatLngBounds.builder();
		builder.include(new LatLng(START.getLatitude(), START.getLongitude()));
		builder.include(new LatLng(END.getLatitude(), END.getLongitude()));
		mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
				50));
	}
	
	/**
	 * ���marker����
	 * 
	 * @param lat
	 * @param lon
	 * @param title
	 * @param snippet
	 * @param icon
	 */
	private void addMarker(double lat, double lon, String title,
			String snippet, BitmapDescriptor icon) {
		MarkerOptions markerOption = new MarkerOptions();
		LatLng markerPoint = new LatLng(lat, lon);
		markerOption.position(markerPoint);
		markerOption.title(title).snippet(snippet);
		markerOption.icon(icon);
		mAMap.addMarker(markerOption);
	}

	/**
	 * ����ϵͳ����̴�
	 * 
	 * @param url
	 * @param title
	 */
	private void shareUrl(String url, String title) {
		
		
		Log.d("tag", url);
		this.url = url;
		/*Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, url);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, title));*/
	}
	
	public void showShare() {
		
		 ShareSDK.initSDK(context);
		 OnekeyShare oks = new OnekeyShare();
		 //�ر�sso��Ȩ
		 oks.disableSSOWhenAuthorize(); 

		// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher, "��һ��");
		 
		 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		 oks.setTitle("����������·");
		 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		 oks.setTitleUrl(url);
		 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		 oks.setText("O(��_��)O����~�������������Σ����������·�ߺ�����Ӵ��");
		 // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		 oks.setImagePath("/phone/icon666.png");//ȷ��SDcard������ڴ���ͼƬ
		 
		 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		 oks.setUrl(url);
		 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		 oks.setComment("O(��_��)O����~�������������Σ������Ƿ�����·�ߺ�����Ӵ��");
		 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		 oks.setSite("��һ��");
		 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		 oks.setSiteUrl(url);

		// ��������GUI
		 oks.show(context);
		 }
}
