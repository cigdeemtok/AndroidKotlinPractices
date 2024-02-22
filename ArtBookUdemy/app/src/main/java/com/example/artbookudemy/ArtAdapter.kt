package com.example.artbookudemy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.artbookudemy.databinding.ItemArtBinding

class ArtAdapter(private val arts : ArrayList<Art>, private val itemClickListener : (Art) -> Unit) : RecyclerView.Adapter<ArtAdapter.ArtHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolder {
        val binding = ItemArtBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtHolder(binding)
    }

    override fun getItemCount() = arts.size

    override fun onBindViewHolder(holder: ArtHolder, position: Int) {
        val art = arts[position]

        holder.binding.itemName.text = art.name
        holder.binding.itemImage.setImageBitmap(art.image)

        holder.binding.root.setOnClickListener {
            itemClickListener(art)
        }

    }

    class ArtHolder(val binding: ItemArtBinding) :RecyclerView.ViewHolder(binding.root) {

    }
}