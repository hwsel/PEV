package io.github.thibaultbee.streampack.camera2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.thibaultbee.streampack.camera2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}