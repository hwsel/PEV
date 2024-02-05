package io.github.thibaultbee.streampack.camera2.ui.fragments

import android.annotation.SuppressLint
import android.media.MediaFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import io.github.thibaultbee.streampack.data.VideoConfig
import io.github.thibaultbee.streampack.ext.srt.streamers.CameraSrtLiveStreamer
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class CameraFragment: Fragment() {

    val client = OkHttpClient()

    private val streamer by lazy {
        CameraSrtLiveStreamer(
            context = requireContext(),
            enableAudio = false
        )
    }
    private val updateFpsHandler = Handler()
    private val updateFpsRunnable: Runnable = Runnable {
        run {
            val req = Request.Builder().url("http://10.0.0.115/fps").build()

            val res = client.newCall(req).execute().body!!.string().toInt()

            val videoConfig = VideoConfig(
                mimeType = MediaFormat.MIMETYPE_VIDEO_AVC,
                resolution = Size(1920, 1080),
                startBitrate = 200 * 1000,
                fps = res,
                gopDuration = 1.0f
            )

            streamer.configure(videoConfig)

            updateFpsHandler.postDelayed(updateFpsRunnable, 1000)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("Fragment", "CameraFragment: onCreate()")



        val videoConfig = VideoConfig(
            mimeType = MediaFormat.MIMETYPE_VIDEO_AVC,
            resolution = Size(1920, 1080),
            fps = 30,
            gopDuration = 1.0f
        )

        streamer.configure(videoConfig)

        streamer.camera = "1"
        streamer.streamId = "#!::r=live/livestream,m=publish"

        streamer.startPreview(null, "1")


        lifecycleScope.launch {

            streamer.startStream("10.0.0.115", 10080)

        }

        updateFpsHandler.postDelayed(updateFpsRunnable, 1000)


    }


}