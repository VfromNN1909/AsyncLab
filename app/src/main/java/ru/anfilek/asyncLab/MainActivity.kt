package ru.anfilek.asyncLab

import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.anfilek.asyncLab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var handlerThread: MyHandlerThread? = null
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener { startHandlerThread() }
        binding.btnStop.setOnClickListener { stopHandlerThread() }
        binding.btnAsync.setOnClickListener { startAsync() }
        binding.btnFreeze.setOnClickListener { freeze() }

        testSharedResources(binding, this)
    }

    private fun startHandlerThread() {
        handlerThread = MyHandlerThread()
        handlerThread?.doWork()
        Toast.makeText(this, "Handler started", Toast.LENGTH_SHORT).show()
    }

    private fun stopHandlerThread() {
        // optional
        handlerThread?.quit()
        Toast.makeText(this, "Handler stopped", Toast.LENGTH_SHORT).show()
    }

    private fun startAsync() {
        MyAsyncTask().execute()
    }

    private fun freeze() {
        Thread.sleep(9000)
    }

    // DO NOT DO THIS! NEVER!
    inner class MyAsyncTask : AsyncTask<String, Int, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            binding.tv.text = result
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
        }

        override fun onCancelled(result: String?) {
            super.onCancelled(result)
        }

        override fun onCancelled() {
            super.onCancelled()
        }

        override fun doInBackground(vararg params: String?): String {
            Thread.sleep(3000)
            return "That's all"
        }
    }
}


