package com.example.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firestore.util.await
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



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


}


