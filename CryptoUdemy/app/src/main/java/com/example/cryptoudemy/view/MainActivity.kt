package com.example.cryptoudemy.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.cryptoudemy.adapter.MyAdapter
import com.example.cryptoudemy.databinding.ActivityMainBinding
import com.example.cryptoudemy.model.CryptoModel
import com.example.cryptoudemy.service.ApiService
import com.example.cryptoudemy.utils.Constants.BASE_URL
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), MyAdapter.Listener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var cryptoList : ArrayList<CryptoModel>
    private lateinit var myAdapter : MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize disposable
        compositeDisposable = CompositeDisposable()

        cryptoList = ArrayList()

        //initialize layout manager
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        //get data from API
        loadData()

    }

    private fun loadData(){
        //initialize retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ApiService::class.java)

        //get data with rx java via disposables
        compositeDisposable.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({list-> handleResponse(list)},
                {throwable-> throwable.localizedMessage?.let { Log.e("Error Rx", it) } })
        )
    }
    private fun handleResponse(cryptoData : List<CryptoModel>){
        cryptoList = ArrayList(cryptoData)

        cryptoList.let {
            myAdapter = MyAdapter(it,this@MainActivity)
            binding.recyclerView.adapter = myAdapter

        }
    }

    override fun itemClickListener(cryptoModel: CryptoModel) {
        Toast.makeText(this@MainActivity,"Clicked",Toast.LENGTH_LONG).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        //clear disposables
        compositeDisposable.clear()
    }
}

