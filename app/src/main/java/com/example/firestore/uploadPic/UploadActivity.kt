package com.example.firestore.uploadPic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firestore.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_upload.*

class UploadActivity : AppCompatActivity() {

     var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        button4.setOnClickListener {
            FirebaseStorage.getInstance().reference.child("images/anh.jpg").putFile(imageUri!!).addOnSuccessListener {
                Toast.makeText(applicationContext,"Da upload",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun chooseImageFromGallery(v: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        if (intent.resolveActivity(packageManager) != null) {

            startActivityForResult(Intent.createChooser(intent,"Choose your image gallery"),2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

}
