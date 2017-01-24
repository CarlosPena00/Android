package cin.mylampada20;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Device extends AppCompatActivity {
    private BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> pairedDevices;
    ListView listView;
    ArrayList list = new ArrayList();
    Context context;

    public static String EXTRA_DEVICE_ADDRESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            btAdapter = BluetoothAdapter.getDefaultAdapter();

        }
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = ((TextView)view).getText().toString();
                String address = info.substring(info.length()-17);
                Toast.makeText(getApplicationContext(),"This is your Mac: " + address, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Device.this,Communication.class);
                intent.putExtra(EXTRA_DEVICE_ADDRESS,address);
                startActivity(intent);
            }
        });

    }


    public void on(View v){
        if(!btAdapter.isEnabled()){
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }
    }
    public void off(View v){
        btAdapter.disable();
    }
    public void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible,0);
    }
    public void stopDiscovery(View v){
        btAdapter.cancelDiscovery();
        Toast.makeText(getApplicationContext(),"Stop Discovery",Toast.LENGTH_SHORT).show();
    }
    public void list(View v){
        pairedDevices = btAdapter.getBondedDevices();
        ArrayList list = new ArrayList();
        int i = 0;
        for(BluetoothDevice bt : pairedDevices){
            list.add(bt.getName() + "\n" + bt.getAddress());
        }
        Toast.makeText(getApplicationContext(),"Showing Paired Devices",Toast.LENGTH_SHORT).show();
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

    }

    public void discovery(View v){
        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        if(btAdapter.startDiscovery()){
            Toast.makeText(getApplicationContext(),"Discovery Start!",Toast.LENGTH_LONG).show();
            final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    // When discovery finds a device
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        System.out.println("Olaaaaaaaaaaaaaaaa");
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        // Add the name and address to an array adapter to show in a ListView
                        list.add(device.getName() + "\n" + device.getAddress());

                    }
                    final ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,list);
                    listView.setAdapter(adapter);
                }
            };
// Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        }
        else Toast.makeText(getApplicationContext(),"False",Toast.LENGTH_SHORT).show();
    }


}
