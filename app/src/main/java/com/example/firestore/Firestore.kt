package com.example.firestore

import com.example.firestore.util.await
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Firestore {

    // tang
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
     fun getDataFlow(): Flow<Data?> =
         channelFlow {
            val subscription = FirebaseFirestore.getInstance().document("datas/data1")
                .addSnapshotListener { snapshot: DocumentSnapshot?, e ->
                    if (e != null) {
                        channel.close(e) // dong
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

            // bỏ dòng ngừng ,data stream , ở firestore là lấy biến listenr registration chấm remove
            awaitClose { subscription.remove() } // nó sẽ xem livedata khi nào k hoạt động nữa thì nó cũng nghỉ
        }

// 1/ tạo flow bằng hàm channelFlow , rồi truyền vào lambda chạy code, trong đó có bắn dữ liệu
    // flow builder , ham nay
@ExperimentalCoroutinesApi // thu vien dang beta, co kha noi
fun checkLogin() : Flow<Boolean> =  channelFlow {
        FirebaseFirestore.getInstance().collection("").get()
            .addOnSuccessListener {
                for (snapshot in it) {
                    if (snapshot.exists()) {
                        channel.offer(true) // no ban
                        break
                    }

                }
            }
    }




    }







