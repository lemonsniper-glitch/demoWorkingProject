package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

//            byte sol = (byte) -0.99;
//            Log.i("prm", String.valueOf(sol));
            GraphView graph = findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Log.i("prm", String.valueOf(bluetoothAdapter.getBondedDevices()));
            Log.i("prm",Integer.toString(2));
            BluetoothDevice hc = bluetoothAdapter.getRemoteDevice("00:21:13:01:F8:8C");
            Log.i("prm",hc.getName());
            BluetoothSocket btsocket = null;
            int count =0;
            do {
                count++;
                try {
                    btsocket = hc.createRfcommSocketToServiceRecord(mUUID);
                    Log.i("prm", String.valueOf(btsocket));
                    btsocket.connect();
                    Log.i("prm", String.valueOf(btsocket.isConnected()));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("prm", String.valueOf(btsocket.isConnected()));
                }
            }while (!btsocket.isConnected() && count<10);

            try {
                OutputStream outputStream = btsocket.getOutputStream();
                int i=0;
                //while (i++<100000) {
                //Log.i("prm","Reached");
                //if(i%5 == 0) {
                outputStream.write(1);
                //}
                //outputStream.write(0);
                //}
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStream inputStream = null;
            try {
                inputStream = btsocket.getInputStream();
                inputStream.skip(inputStream.available());
                Log.i("prm","Reached");
                int hold = 0;
                while(hold<100){
                    byte a = (byte)inputStream.read();
                    //Toast.makeText(this, String.valueOf(a), Toast.LENGTH_SHORT).show();
                    Log.i("prm",String.valueOf(a));
                    series.appendData(new DataPoint(hold++,a),true,100);
                    graph.addSeries(series);
                    graph.getViewport().setMinX(0);
                    graph.getViewport().setMaxX(100);
                    System.out.println(a);
                }
                //Log.isLoggable("prm",a);
                series.setColor(Color.WHITE);



            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                btsocket.close();
                Log.i("prm","Socket closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }