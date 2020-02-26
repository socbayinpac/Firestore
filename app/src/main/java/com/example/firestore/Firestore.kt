package com.example.firestore

import com.example.firestore.util.await
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow

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

     @ExperimentalCoroutinesApi
     fun getDataFlow(): Flow<Data?> {

        return channelFlow {
            val subscription = FirebaseFirestore.getInstance().document("datas/data1")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        channel.close(e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val book = snapshot.toObject(Data::class.java)
                        book?.let {
                            channel.offer(it)
                        }
                    } else {
                        channel.offer(null)
                    }
                }
            awaitClose { subscription.remove() }
        }

    }

}
