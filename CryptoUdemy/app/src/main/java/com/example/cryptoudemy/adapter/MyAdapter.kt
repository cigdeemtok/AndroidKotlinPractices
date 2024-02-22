package com.example.cryptoudemy.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoudemy.databinding.ItemRowBinding
import com.example.cryptoudemy.model.CryptoModel

class MyAdapter(private val cryptoList : ArrayList<CryptoModel>, private val listener : Listener) : RecyclerView.Adapter<MyAdapter.ItemHolder>() {

    interface Listener {
        fun itemClickListener(cryptoModel: CryptoModel)
    }

    private val colors : Array<String> = arrayOf("#2f0070","#4c04ae","#6f06fe","#8a35fd","#b680fe")

    class ItemHolder(val binding : ItemRowBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ItemHolder(binding)
    }

    override fun getItemCount() = cryptoList.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var data = cryptoList.get(position)
        holder.binding.textCryptoName.text = data.currency
        holder.binding.textCryptoPrice.text = data.price

        holder.itemView.setBackgroundColor(Color.parseColor(colors[position % 5]))

        holder.itemView.setOnClickListener {
            listener.itemClickListener(data)
        }
    }
}