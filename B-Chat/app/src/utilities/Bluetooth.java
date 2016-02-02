package utilities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

/**
 * Created by Firdaus.E.D on 26/01/16.
 */
public class Bluetooth extends Activity {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    Intent enableIntent = new Intent(
            BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    )

    Intent discoverableIntent = new Intent(
            BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    discoverableIntent.putExtra(
            BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
    startActivity(discoverableIntent);
    )

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
}

