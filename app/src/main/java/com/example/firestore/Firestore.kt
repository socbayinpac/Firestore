package com.example.firestore

import android.util.Log
import com.example.firestore.util.await
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class Firestore {

    suspend fun getData() : Data? {
        var snapshot: DocumentSnapshot? = null
        try {
           snapshot = FirebaseFirestore.getInstance().document("datas/data1").get().await()
        } catch(e: Exception) {
            snapshot = null
        }


        return snapshot?.toObject(Data::class.java) // null la doc k ton tai

    }

}
