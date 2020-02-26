package com.example.firestore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.lang.Exception

class MainViewModel : ViewModel() {

    val firestore = Firestore() // tham chieu tang truoc
    val livedata = MutableLiveData<Data>()

    val job = Job()

    val viewmodelScope = CoroutineScope(Dispatchers.Main + job)

    init {
        loadData()
    }

    fun loadData() {
        viewmodelScope.launch {
            try {
                val data1 = firestore.getData()
                if (data1 != null) { // null la k ton tai object tren server
                    livedata.postValue(data1)
                }
            } catch (e: Exception) {
                Log.e("khong ton tai", "dqwdq")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewmodelScope.cancel()
    }
}