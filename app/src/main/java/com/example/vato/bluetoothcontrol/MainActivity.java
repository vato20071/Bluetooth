package com.example.vato.bluetoothcontrol;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private String[] permissionList = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    private void getPermissions() {
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {permission}, 1);
            }
        }
    }

    private List<Finger> fingerList;
    private FingerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initFingerList();
        initFingerAdapter();
    }

    private void initFingerAdapter() {
        ListView view = (ListView) findViewById(R.id.fingers);
        assert view != null;
        adapter = new FingerListAdapter(this, R.id.fingers, fingerList);
        view.setAdapter(adapter);
        Button button = (Button) findViewById(R.id.save_button);
        assert button != null;
        button.setOnClickListener(new ButtonClickListener());
    }

    private void initFingerList() {
        fingerList = new ArrayList<>();
        for (int i=0; i<5; i++) {
            fingerList.add(new Finger(i+1, 0));
        }
    }

    protected void submitFingers(List<Finger> fingerList) {
        for (int i=0; i<fingerList.size(); i++) {
            try {
                write("a");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private OutputStream outputStream;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e("TEMP", "Could not create Insecure RFComm Connection",e);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    private void init() throws Exception {

        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> pairedDevices = blueAdapter.getBondedDevices();
                String deviceAddress = "";
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        if (device.getName().equals("RNBT-7105")) {
                            deviceAddress = device.getAddress();
                        }
                    }
                }
                BluetoothDevice current = blueAdapter.getRemoteDevice(deviceAddress);

                BluetoothConnector connector = new BluetoothConnector(current, true, blueAdapter, null);
                connector.connect();

            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            List<Finger> fingers = new ArrayList<>();
            for (int i=0; i<adapter.getCount(); i++) {
                fingers.add(adapter.getItem(i));
            }
            submitFingers(fingers);
        }
    }
}
