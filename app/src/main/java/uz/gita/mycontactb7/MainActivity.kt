package uz.gita.mycontactb7

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactb7.domain.ContactRepository
import uz.gita.mycontactb7.utils.MyEventBus
import uz.gita.mycontactb7.utils.NetworkStatusValidator
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: ContactRepository

    @Inject
    lateinit var networkStatusValidator: NetworkStatusValidator
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkStatusValidator.init(
            availableNetworkBlock = {
                executor.execute {
                    repository.syncWithServer(
                        finishBlock = {
                            this@MainActivity.runOnUiThread {
                                MyEventBus.reloadEvent?.invoke()
                            }
                        },
                        errorBlock = {
                            this@MainActivity.runOnUiThread {
                                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            },
            lostConnection = { Toast.makeText(this@MainActivity, "Not connection", Toast.LENGTH_SHORT).show() }
        )
    }
}