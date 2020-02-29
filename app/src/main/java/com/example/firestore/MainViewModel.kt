package com.example.firestore

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {

    val firestore = Firestore() // tham chieu tang truoc
    val livedata = firestore.getDataFlow().asLiveData() // cach 2 dùng asLiveData
    val livedata2  = MutableLiveData<Data?>()


    init {
       // loadDataFlow()
        loadData()
    }

    //load data thời gian thực với flow
    //cách 1 dùng .collect {}
    
//    fun loadDataFlow() {
//        viewModelScope.launch {
//            firestore.getDataFlow().flowOn(Dispatchers.IO).collect {
//                it?.let {
//                    livedata.postValue(it) // cap nhat livedata
//                }
//            }
//        }
//    }

    // load data một lần với suspend
    fun loadData() {
        viewModelScope.launch {
            try {
                val data1 = firestore.getData() // return data 1 lan
                if (data1 != null) { // null la k ton tai object tren server
                    livedata2.postValue(data1)
                }
            } catch (e: Exception) {
                Log.e("khong ton tai", "dqwdq")
            }
        }
    }

}