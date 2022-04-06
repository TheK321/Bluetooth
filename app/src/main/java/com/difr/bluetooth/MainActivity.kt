package com.difr.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.difr.bluetooth.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var bAdapter:BluetoothAdapter
    lateinit var binding:ActivityMainBinding

    private val REQUEST_CODE_ENABLE_BT:Int = 1
    private val REQUEST_CODE_DISCOVERABLE_BT:Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btOff.setBackgroundColor(Color.parseColor("#FFC4C4C4"))
        binding.btSearch.setBackgroundColor(Color.parseColor("#FF9C9B9B"))
        binding.btKnow.setBackgroundColor(Color.parseColor("#FF676666"))

        binding.btOff.isEnabled = false
        binding.btSearch.isEnabled = false
        binding.btKnow.isEnabled = false

        bAdapter = BluetoothAdapter.getDefaultAdapter()

        if(bAdapter.isEnabled){
            binding.ivBluetooth.setImageResource(R.drawable.bt_on)
        }else{
            binding.ivBluetooth.setImageResource(R.drawable.bt_off)
        }

        binding.btOn.setOnClickListener(){
            if(bAdapter.isEnabled){
                Toast.makeText(this,"ENCENDIDO",Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent,REQUEST_CODE_ENABLE_BT)
            }
        }
        binding.btOff.setOnClickListener(){
            if(!bAdapter.isEnabled){
                Toast.makeText(this,"APAGADO",Toast.LENGTH_SHORT).show()
            }else{
                binding.btOn.setBackgroundColor(Color.parseColor("#08508A"))
                binding.btOff.setBackgroundColor(Color.parseColor("#FFC4C4C4"))
                binding.btSearch.setBackgroundColor(Color.parseColor("#FF9C9B9B"))
                binding.btKnow.setBackgroundColor(Color.parseColor("#FF676666"))

                binding.btOn.isEnabled = true
                binding.btOff.isEnabled = false
                binding.btSearch.isEnabled = false
                binding.btKnow.isEnabled = false
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                }
                bAdapter.disable()

                binding.tvDevices.text = ""
                binding.ivBluetooth.setImageResource(R.drawable.bt_off)
                Toast.makeText(this,"BLUETOOTH APAGADO",Toast.LENGTH_SHORT).show()
            }
        }
        binding.btSearch.setOnClickListener(){
            if(!bAdapter.isDiscovering){
                Toast.makeText(this,"HABILITANDO VISIBILIDAD",Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                startActivityForResult(intent,REQUEST_CODE_DISCOVERABLE_BT)
            }
        }
        binding.btKnow.setOnClickListener(){
            Thread.sleep(1000)
            if(bAdapter.isEnabled){
                binding.tvDevices.text = "DISPOSITIVO VINCULADO"
                val dispositivos = bAdapter.bondedDevices
                for(device in dispositivos){
                    val dispNombre = device.name
                    val dispDireccion = device
                    binding.ivBluetooth.setImageResource(R.drawable.bt_con)
                    binding.tvDevices.append("\n$device , $device")

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_ENABLE_BT ->
                if(resultCode == Activity.RESULT_OK){
                    binding.ivBluetooth.setImageResource(R.drawable.bt_on)
                    Toast.makeText(this,"BLUETOOTH ENCENDIDO",Toast.LENGTH_SHORT).show()
                    binding.btOn.isEnabled = false
                    binding.btOff.isEnabled = true
                    binding.btSearch.isEnabled = true
                    binding.btKnow.isEnabled = true

                    binding.btOn.setBackgroundColor(Color.parseColor("#FFE0E0E0"))
                    binding.btOff.setBackgroundColor(Color.parseColor("#297DC1"))
                    binding.btSearch.setBackgroundColor(Color.parseColor("#60AEEC"))
                    binding.btKnow.setBackgroundColor(Color.parseColor("#8BBADF"))
                }else{
                    Toast.makeText(this,"BLUETOOTH NO SE PUEDE HABILITAR",Toast.LENGTH_SHORT).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}