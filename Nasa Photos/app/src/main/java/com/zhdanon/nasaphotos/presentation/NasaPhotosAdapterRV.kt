package com.zhdanon.nasaphotos.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zhdanon.nasaphotos.R
import com.zhdanon.nasaphotos.data.photo.PhotoDto
import com.zhdanon.nasaphotos.databinding.ItemNasaPhotoBinding

class NasaPhotosAdapterRV(
    private val onClick: (PhotoDto) -> Unit
) : PagingDataAdapter<PhotoDto, NasaPhotosAdapterRV.NasaViewHolder>(DiffUtilCallback()) {

    override fun onBindViewHolder(holder: NasaViewHolder, position: Int) {
        val item = getItem(position)
        val myLink = if (item?.imgSrc!!.contains("http://mars.jpl.nasa.gov")) {
            val temp = item.imgSrc.removePrefix("http://mars.jpl.nasa.gov")
            "https://mars.nasa.gov$temp"
        } else {
            item.imgSrc
        }
        with(holder.binding) {
            item.let {
                Glide.with(photo.context)
                    .load(myLink)
                    .placeholder(R.drawable.loading_placeholder)
                    .into(holder.binding.photo)
                holder.binding.roverName.text = "Rover: ${item.rover.name}"
                holder.binding.cameraName.text = "Camera: ${item.camera.name}"
                holder.binding.sol.text = "Sol: ${item.sol}"
                holder.binding.earthDate.text = "Date: ${item.earthDate}"
            }
        }
        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NasaViewHolder {
        val binding =
            ItemNasaPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NasaViewHolder(binding)
    }

    inner class NasaViewHolder(val binding: ItemNasaPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)
}

class DiffUtilCallback : DiffUtil.ItemCallback<PhotoDto>() {
    override fun areItemsTheSame(oldItem: PhotoDto, newItem: PhotoDto): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PhotoDto, newItem: PhotoDto): Boolean =
        oldItem == newItem
}