package dk.dtu.elektro.playware.fable;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    ArrayAdapter<String> listAdapter;
    Button connectNew;
    ListView listView;
    boolean scanOn = false;
    private BleWrapper mBleWrapper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        
        mBleWrapper = new BleWrapper(this, new BleWrapperUiCallbacks.Null()
        {
            @Override
            public void uiDeviceFound(final BluetoothDevice device,
                                      final int rssi,
                                      final byte[] record
                                     )
            {
                String msg = "uiDeviceFound: "+device.getName()+", "+rssi+", "+String.valueOf(rssi);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                Log.d("DEBUG", "uiDeviceFound: " + msg);
            }
        });

        if (mBleWrapper.checkBleHardwareAvailable() == false)
        {
            Toast.makeText(this, "No BLE-compatible hardware detected",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void init() {
        connectNew=(Button)findViewById(R.id.bConnectNew);
        listView=(ListView)findViewById(R.id.listView);
        listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
        listView.setAdapter(listAdapter);

    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onRestart() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        // check for Bluetooth enabled on each resume
        if (mBleWrapper.isBtEnabled() == false)
        {
        // Bluetooth is not enabled. Request to user to turn it on
            Intent enableBtIntent = new Intent(BluetoothAdapter.
                    ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            finish();
        }
        // init ble wrapper
        mBleWrapper.initialize();
    }


   public void onScanClicked(View view) {
       // Toggles Scanning for bluetooth LE devices

       if (scanOn) {
            // Start Scan
           mBleWrapper.startScanning();
           scanOn=true;
       } else {
            // Stop Scan
           mBleWrapper.stopScanning();
           scanOn=false;
       }

   }













    @Override
    protected void onPause() {
        super.onPause();

        mBleWrapper.diconnect();
        mBleWrapper.close();
    }

    @Override
    protected void onStop() {

    }

    @Override
    protected void onDestroy() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
