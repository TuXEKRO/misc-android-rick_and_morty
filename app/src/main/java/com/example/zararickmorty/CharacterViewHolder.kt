package com.example.zararickmorty

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.zararickmorty.databinding.ItemCharacterBinding
import com.squareup.picasso.Picasso

class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemCharacterBinding.bind(view)

    fun bind(character: Character) {
        Picasso.get().load(character.image).into(binding.ivCharacter)
        binding.tvName.text = character.name
        binding.tvOrigin.text = character.location.name
    }
}