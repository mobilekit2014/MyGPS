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
            	// ��ġ������ ����
            	LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        		
            	// ��ġ������ ����
        		GPSListener gpsListener = new GPSListener();
        		
        		long minTime = 1000; // ������Ʈ �ð� ����
        		float minDistance = 0; // ������Ʈ �Ÿ� ��� ����
        		
        		// ��ġ ������Ʈ ��û
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
        		Toast.makeText(getApplicationContext(), "��ġ Ȯ�� ����", Toast.LENGTH_LONG).show();
        		
        		Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
    	        
    	        
    	        if (lastLocation != null) { 
    	            Double latitude = lastLocation.getLatitude(); 
    	            Double longitude = lastLocation.getLongitude(); 
    	  
    	            Toast.makeText(getApplicationContext(), "�ֱ� ��ġ : " + "���� : "+ latitude + "\n�浵 : "+ longitude, Toast.LENGTH_LONG).show(); 
    	       
    	        }
            }
            
        }); 
        
        setGPS(); // �ʱ� GPS ���� ���� Ȯ��
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// GPS ����ȭ���� ����ִ� �Լ�
	private void setGPS(){
		// ����Ʈ ���ι��̴��� ������ ����Ʈ�� ã���ִ� ��ü
		ContentResolver cr = getContentResolver();
		
		//GPS�� �����ִ��� �����ִ��� Ȯ��
		boolean isEnable = Settings.Secure.isLocationProviderEnabled(cr, LocationManager.GPS_PROVIDER);
		
		if(!isEnable){
			// ���â �������� ���
			new AlertDialog.Builder(this)
			.setTitle("GPS ����")                                             // ���â ����
			.setMessage("GPS�� ���� �ֽ��ϴ�. \nGPS�� �Ѱڽ��ϱ�? ")             // ���â �޽���
			.setPositiveButton("��", new DialogInterface.OnClickListener() {  // ���â ��ư ����
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// GPS ����ȭ���� ����.
					Intent setIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(setIntent);
				}
			})
			.setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() {  // ���â ��ư ����
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "GPS�� ���� �ֽ��ϴ�.", Toast.LENGTH_LONG).show();
		}
	}
	
	// ��ġ������ ����
	private class GPSListener implements LocationListener{
		public void onLocationChanged(Location location){
			Double latitude = location.getLatitude();
			Double longitude = location.getLongitude();
	
			
			String msg = "���� : " + latitude + "\n�浵 : " + longitude;
			Log.i("GPSListener", msg);
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
		
		public void onProviderDisabled(String provider) {}
		
		public void onProviderEnabled(String provider) {}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {} 
	}

}
