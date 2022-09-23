package com.zhdanon.rickandmortyapi.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zhdanon.rickandmortyapi.R
import com.zhdanon.rickandmortyapi.data.characters.ResultCharacterDto
import com.zhdanon.rickandmortyapi.databinding.ItemCharacterBinding

class RaMAdapterRV(
    private val onClick: (ResultCharacterDto) -> Unit
) :
    PagingDataAdapter<ResultCharacterDto, RaMAdapterRV.RaMViewHolder>(
        DiffUtilCallback()
    ) {

    inner class RaMViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: RaMViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            item.let {
                Glide.with(avatar.context)
                    .load(it?.image)
                    .into(holder.binding.avatar)
                holder.binding.characterName.text = it?.name
                holder.binding.characterStatus.text =
                    holder.binding.root.context.getString(R.string.status_line, it?.status, it?.species)
                holder.binding.characterLocation.text = it?.location?.name
            }
        }
        holder.binding.root.setOnClickListener { onClick(item!!) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaMViewHolder {
        val binding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RaMViewHolder(binding)
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<ResultCharacterDto>() {
    override fun areItemsTheSame(
        oldItem: ResultCharacterDto,
        newItem: ResultCharacterDto
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: ResultCharacterDto,
        newItem: ResultCharacterDto
    ): Boolean =
        oldItem == newItem
}