package uz.gita.mycontactb7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.gita.mycontactb7.utils.NetworkStatusValidator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkStatusValidator.init(
            context = this,
            availableNetworkBlock = {
                //
            }
        )
    }
}