package cin.mylampada20;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Communication extends AppCompatActivity {

    Button sendOne, sendZero;
    private EditText editText;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
//    private static final UUID MY_UUID = UUID.randomUUID();
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public String newAddress = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        //addkey

        sendZero = (Button)findViewById(R.id.buttonSend0);
        sendOne  = (Button)findViewById(R.id.buttonSend1);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Intent intent= getIntent();
        newAddress = intent.getStringExtra(Device.EXTRA_DEVICE_ADDRESS);
        checkBTState();
        BluetoothDevice device = btAdapter.getRemoteDevice(newAddress);
        try{
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        }catch (IOException e1){
            Ttoast("ERRO - BTSOCKET");
        }
        try{
            btSocket.connect();
        }catch (IOException e2){
            Ttoast("ERRO - Connect");
            try{
                btSocket.close();
            }catch (IOException e3){
                Ttoast("ERRO Closing");
            }

        }
        try{
            outStream = btSocket.getOutputStream();
        }catch (IOException e4){
            Ttoast("ERRO OUTSTREAM");
        }
        sendData("X");

    }
    @Override
    public void onPause(){
        super.onPause();
        try{
            btSocket.close();
        }catch (IOException e4){
            Ttoast("Erro Closing onPause");
        }
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
        throws IOException{
            return device.createRfcommSocketToServiceRecord(MY_UUID);
        }


    public void Ttoast(String myString){
        Toast.makeText(getApplicationContext(),myString,Toast.LENGTH_SHORT).show();
    }
    public void sendNumberZero(View view){
        sendData("0");
    }

    public void sendNumberOne(View view){
        sendData("1");
    }
    public void sendCharA(View view){
        sendData("A");
    }

    public void sendCharB(View view){
        sendData("B");
    }

    public void sendCharC(View view){
        sendData("C");
    }

    public void sendCharD(View view){
        sendData("D");
    }

    public void sendCharE(View view){
        sendData("E");
    }
    public void sendCharF(View view){
        sendData("F");
    }

    public void sendCharG(View view){
        sendData("G");
    }

    private void checkBTState(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter==null){
            Toast.makeText(getApplicationContext(),"Device Do not have support for BlueTooth",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            if(!btAdapter.isEnabled())
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,1);
            }
        }
    }

    private void sendData (String message){
        byte[] msgBuffer = message.getBytes();

        try{
            outStream.write(msgBuffer);
        }catch (IOException e){
            Toast.makeText(getApplicationContext(),"Erro device 404",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}