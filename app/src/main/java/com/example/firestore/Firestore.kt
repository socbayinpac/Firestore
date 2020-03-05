package com.example.firestore

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Firestore {


    // lấy data một lần bằng suspendCancellableCoroutine
    suspend fun getData() = suspendCancellableCoroutine<Data?> { cont ->
        FirebaseFirestore.getInstance().document("datas/data1").get()
            .addOnSuccessListener {
                // thích làm gì ở đây làm
                cont.resume(it.toObject(Data::class.java)) // trả data về (hoạt động như return)
            }
            .addOnCanceledListener {
                // thích làm gì ở đây làm
                cont.cancel() // hủy
            }
            .addOnFailureListener {
                // thích làm gì ở đây làm
                cont.resumeWithException(it) // quăng exception
            }
    }
    // lấy data thời gian thực bằng flow
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
                        book?.let {data ->
                            channel.offer(data)
                        }
                    } else {
                        channel.offer(null)
                    }
                }
            // bỏ dòng ngừng ,data stream , ở firestore là lấy biến listenr registration chấm remove
            awaitClose { subscription.remove() }  // nó sẽ xem livedata khi nào k hoạt động nữa thì nó cũng nghỉ
        }


    fun updateManyField() {


    }

    suspend fun addData(data: Data) = suspendCancellableCoroutine<Boolean> { cont ->
        val docRef = FirebaseFirestore.getInstance().document("")
        docRef.get()
            .addOnSuccessListener {
                if (!it.exists()) {
                    docRef.set(data)
                    cont.resume(true)
                }
                cont.resume(false)
            }
            .addOnFailureListener {
                cont.resume(true)
            }
    }


    fun muaItem() {
        val accTrai = FirebaseFirestore.getInstance().document("accountT")
        val accPhai = FirebaseFirestore.getInstance().document("accountP")
        val db = FirebaseFirestore.getInstance()
        db.runTransaction { transaction ->
            val soDuHienTaiT = transaction.get(accTrai).get("tien") as Int
             transaction.update(accPhai,"tien",soDuHienTaiT-2) // tru tien trai

            val soDuHienTaiP = transaction.get(accPhai).get("tien") as Int
            transaction.update(accPhai,"tien",soDuHienTaiT+2)// cong tien phai

            transaction.update(accPhai,"item",null) // tru item cua pahai
            transaction.update(accTrai,"item",Any()) // cong item cua trai

            true
        }.addOnSuccessListener { isSuccess ->
            if (isSuccess == true) {
                // true

            } else {
                // ban false
            }
        }.addOnFailureListener {

        }


    }



}







