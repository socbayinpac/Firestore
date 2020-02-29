package com.example.firestore

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    lateinit var registration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.livedata.observe(this, Observer {
            val value = viewModel.livedata.value
            textView.setText(value.toString())
            Toast.makeText(applicationContext,"da load",Toast.LENGTH_SHORT).show()
        })


        button.setOnClickListener {
            Toast.makeText(applicationContext,viewModel.livedata.value.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
     //   registration.remove() // de de ton 3g, pin, cpu
    }


}


