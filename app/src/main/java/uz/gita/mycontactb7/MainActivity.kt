package uz.gita.mycontactb7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import uz.gita.mycontactb7.domain.ContactRepository
import uz.gita.mycontactb7.domain.impl.ContactRepositoryImpl
import uz.gita.mycontactb7.utils.MyEventBus
import uz.gita.mycontactb7.utils.NetworkStatusValidator

class MainActivity : AppCompatActivity() {
    private val repository : ContactRepository by lazy { ContactRepositoryImpl.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkStatusValidator.init(
            context = this,
            availableNetworkBlock = {
                repository.syncWithServer(
                    finishBlock = {
                        MyEventBus.reloadEvent?.invoke()
                    },
                    errorBlock =  {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }
}