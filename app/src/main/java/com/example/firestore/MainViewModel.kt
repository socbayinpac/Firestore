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
  //  val livedata2 = MutableLiveData<Data>()
    val livedata = firestore.getDataFlow().asLiveData() // cach 2
//    val showToast = MutableLiveData<Boolean>().apply {
//        value = false
//    }

    //
     val showToast2: LiveData<Boolean> = firestore.checkLogin().asLiveData() // cach nhanh
    init {
       // loadData()
     //   loadDataFlow()
        // hieu lam bam

//    viewModelScope.launch {
//        // khi data dc BAN thi goi lai collect
//       val flow = firestore.checkLogin()
//        .collect {
//            showToast.value = it
//        }
//        // collect chi nam trong coroutine
//    }

    }


    fun loadDataFlow() {

        // 3 luong co ban

//        viewModelScope.launch {
//            firestore.getDataFlow().flowOn(Dispatchers.IO).collect {
//                it?.let {
//                    livedata.postValue(it) // cap nhat livedata
//                }
//            }
//        }
    }

//    fun loadData() {
//        viewModelScope.launch {
//            try {
//                val data1 = firestore.getData() // return data 1 lan
//                if (data1 != null) { // null la k ton tai object tren server
//                    livedata2.postValue(data1)
//                }
//            } catch (e: Exception) {
//                Log.e("khong ton tai", "dqwdq")
//            }
//        }
//    }

}