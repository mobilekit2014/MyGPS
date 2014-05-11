package kr.ac.kumoh;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnSetGPS = (Button) findViewById(R.id.btnSettingGPS);
		Button btnShowLocation = (Button) findViewById(R.id.btnLocation); 
		
		btnSetGPS.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setGPS();
			}
		});
		
        btnShowLocation.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) { 
            	// 위치관리자 생성
            	LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        		
            	// 위치리스너 생성
        		GPSListener gpsListener = new GPSListener();
        		
        		long minTime = 1000; // 업데이트 시간 간격
        		float minDistance = 0; // 업데이트 거리 허용 오차
        		
        		// 위치 업데이트 요청
        		manager.requestLocationUpdates(        // GPS_PROVIDER
        				LocationManager.GPS_PROVIDER, 
        				minTime, 
        				minDistance, 
        				gpsListener);
        		/*
        		manager.requestLocationUpdates(       // NETWORK_PROVIDER
        	               LocationManager.NETWORK_PROVIDER, 
        	               minTime, 
        	               minDistance, 
        	               gpsListener);
        	               */
        		Toast.makeText(getApplicationContext(), "위치 확인 시작", Toast.LENGTH_LONG).show();
        		
        		Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
    	        
    	        
    	        if (lastLocation != null) { 
    	            Double latitude = lastLocation.getLatitude(); 
    	            Double longitude = lastLocation.getLongitude(); 
    	  
    	            Toast.makeText(getApplicationContext(), "최근 위치 : " + "위도 : "+ latitude + "\n경도 : "+ longitude, Toast.LENGTH_LONG).show(); 
    	       
    	        }
            }
            
        }); 
        
        setGPS(); // 초기 GPS 설정 여부 확인
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// GPS 설정화면을 띄워주는 함수
	private void setGPS(){
		// 컨텐트 프로바이더에 설정된 콘텐트를 찾아주는 객체
		ContentResolver cr = getContentResolver();
		
		//GPS가 켜져있는지 꺼져있는지 확인
		boolean isEnable = Settings.Secure.isLocationProviderEnabled(cr, LocationManager.GPS_PROVIDER);
		
		if(!isEnable){
			// 경고창 형식으로 출력
			new AlertDialog.Builder(this)
			.setTitle("GPS 설정")                                             // 경고창 제목
			.setMessage("GPS가 꺼져 있습니다. \nGPS를 켜겠습니까? ")             // 경고창 메시지
			.setPositiveButton("예", new DialogInterface.OnClickListener() {  // 경고창 버튼 생성
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// GPS 설정화면을 띄운다.
					Intent setIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(setIntent);
				}
			})
			.setNegativeButton("아니오", new DialogInterface.OnClickListener() {  // 경고창 버튼 생성
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "GPS가 켜져 있습니다.", Toast.LENGTH_LONG).show();
		}
	}
	
	// 위치리스너 정의
	private class GPSListener implements LocationListener{
		public void onLocationChanged(Location location){
			Double latitude = location.getLatitude();
			Double longitude = location.getLongitude();
	
			
			String msg = "위도 : " + latitude + "\n경도 : " + longitude;
			Log.i("GPSListener", msg);
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
		
		public void onProviderDisabled(String provider) {}
		
		public void onProviderEnabled(String provider) {}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {} 
	}

}
