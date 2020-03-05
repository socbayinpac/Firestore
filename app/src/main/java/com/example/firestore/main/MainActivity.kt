package com.example.firestore.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.firestore.R
import com.example.firestore.uploadPic.UploadActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.settings_activity.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

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


        button3.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        signout.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnSuccessListener {
                Toast.makeText(applicationContext,"Da dang xuat",Toast.LENGTH_SHORT).show()
            }
        }


        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            Toast.makeText(applicationContext,"---${currentUser.uid} ",Toast.LENGTH_LONG).show()
        } else {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.PhoneBuilder().setDefaultNumber("vn","936998493").build()
                ))
                .build(),123)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                Toast.makeText(applicationContext,"---${currentUser?.uid} ",Toast.LENGTH_LONG).show()
            }
        }
    }




}


