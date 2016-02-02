package utilities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.bluetooth.Chat.R;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Firdaus.E.D on 26/01/16.
 */
public class Connection {
    private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)){

                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (device.getBondState() !=BluetoothDevice.BOND_BONDED) {
                newDeviceArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                .equals(action)){
                setProgressBarInderterminateVisibility(false);
                setTitle(R.string.select_device);
                if (newDevicesArrayAdapter.getCount()== 0){
                    String noDevices = getResources().getText(
                            R.string.none_found).toString();
                    newDevicesArrayAdapter.add(noDevices);



            }
        }
    }
};

IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver(discoveryFinishReceiver, filter);

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;
        private String socketType;

        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            socketType = secure ? "Secure" : "Insecure";

            try {
                if (secure){
                    tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);

                }else {
                    tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_INSECURE);

                }
            }catch (IOException e){
        }
            ServerSocket = tmp;
    }
        public void run() {
            setName("AcceptThread" + socketType);
            BluetoothSocket socket = null;
            while (state != STATE_CONNECTED) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    break;
                }
                if (socket != null) {
                    synchronized (ChatService.this) {
                        switch (state) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                connected(socket, socket.getRemoteDevice(), socketType);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:

                                try {
                                    socket.close();
                                } catch (IOException e) {
                                }
                                break;
                        }

                    }
                }
            }
        }
        public void cancel(){
        try{
            serverSocket.close();
        }catch (IOException e){
        }
    }
        private class ConnectThread extends Thread{
            private final BluetoothSocket socket;
            private final BluetoothDevice device;
            private String socketType;

            public ConnectThread(BluetoothDevice device, boolean secure){
                this.device = device;
                BluetoothSocket tmp = null;
                socketType = secure ? "Secure" : "Insecure";

                try{
                    if (secure){
                        tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                    }
                }else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                }
            }catch (IOException e){
        }
            socket = tmp;
    }
        public void run(){
        setName("ConnectThread" + socketType);
        bluetoothAdapter.cancelDiscovery();
            try{
                socket.connect();
            }catch (IOException e){

            }
            connectionFailed();
            return;
        }
        synchronized (ChatService.this){
            connectThread = null;
        }
        connected(socket, device, socketType);
    }
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
