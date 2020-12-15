package io.github.joshuadeguzman.feedly_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Without caching, without data
//        val btRegister = findViewById<Button>(R.id.btRegister =)
//        btRegister.setOnClickListener {
//            startActivity(
//                FlutterActivity.createDefaultIntent(this)
//            )
//        }

        // With caching, without data
//        val btRegister = findViewById<Button>(R.id.btRegister =)
//        btRegister.setOnClickListener {
//            startActivity(
//                FlutterActivity.withCachedEngine("feedly_engine").build(this)
//            )
//        }

        // With caching, with data
        val flutterEngine = (application as FeedlyApplication).flutterEngine
        val channel =
            MethodChannel(
                flutterEngine.dartExecutor.binaryMessenger,
                "feedly/registration_channel"
            )
        channel.setMethodCallHandler { methodCall: MethodCall, result: MethodChannel.Result ->
            if (methodCall.method == "logout_user") {
                super.onBackPressed()
                print("From Flutter: ${methodCall.arguments.toString()}")
                result.success("To Android: Logging out the user!")
            } else {
                result.notImplemented()
            }
        }

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)

        val btRegister = findViewById<Button>(R.id.btRegister)
        btRegister.setOnClickListener {
            startActivity(
                FlutterActivity.withCachedEngine("feedly_engine").build(this)
            )
            channel.invokeMethod("register_user", "${etFirstName.text} ${etLastName.text}")
        }
    }
}