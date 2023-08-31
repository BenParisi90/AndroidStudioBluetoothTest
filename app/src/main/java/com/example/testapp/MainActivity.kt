package com.example.testapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testapp.ui.theme.TestAppTheme
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BluetoothControl()
                }
            }
        }
    }
}

@Composable
fun BluetoothControl() {
    var bluetoothSocket by remember { mutableStateOf<BluetoothSocket?>(null) }

    val device = BluetoothAdapter.getDefaultAdapter()?.bondedDevices?.find {
        it.name == "HC-06"
    }

    if (device != null) {
        if (bluetoothSocket == null) {
            bluetoothSocket = connectToDevice(device)
        }
    }

    Column {
        Button(
            onClick = { sendCommand("Btn_A;", bluetoothSocket) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Btn_A")
        }

        Button(
            onClick = { sendCommand("Btn_B;", bluetoothSocket) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Btn_B")
        }

        Button(
            onClick = { sendCommand("Btn_C;", bluetoothSocket) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Btn_C")
        }
    }
}

private fun connectToDevice(device: BluetoothDevice): BluetoothSocket? {
    val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    return try {
        val socket = device.createRfcommSocketToServiceRecord(uuid)
        socket.connect()
        socket
    } catch (e: IOException) {
        null
    }
}

private fun sendCommand(command: String, socket: BluetoothSocket?) {
    if (socket == null) {
        return
    }

    try {
        val outputStream: OutputStream = socket.outputStream
        outputStream.write(command.toByteArray())
    } catch (e: IOException) {
        // Handle the exception
    }
}

@Preview(showBackground = true)
@Composable
fun BluetoothControlPreview() {
    TestAppTheme {
        BluetoothControl()
    }
}
