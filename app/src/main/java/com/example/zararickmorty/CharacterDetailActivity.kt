package com.example.zararickmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CharacterDetailActivity : AppCompatActivity() {
    private lateinit var characterImage: ImageView
    private lateinit var characterName: TextView
    private lateinit var characterStatus: TextView
    private lateinit var characterSpecies: TextView
    private lateinit var characterType: TextView
    private lateinit var characterGender: TextView
    private lateinit var characterOriginName: TextView
    private lateinit var characterLocationName: TextView
    private lateinit var characterEpisode: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)

        characterImage = findViewById(R.id.ivCharacterDetail)
        characterName = findViewById(R.id.tvNameCharacterDetail)
        characterStatus = findViewById(R.id.tvStatusCharacterDetail)
        characterSpecies = findViewById(R.id.tvSpeciesCharacterDetail)
        characterType = findViewById(R.id.tvTypeCharacterDetail)
        characterGender = findViewById(R.id.tvGenderCharacterDetail)
        characterOriginName = findViewById(R.id.tvOriginNameCharacterDetail)
        characterLocationName = findViewById(R.id.tvLocationNameCharacterDetail)
        characterEpisode = findViewById(R.id.tvEpisodeCharacterDetail)

        val characterId = intent.getIntExtra("characterId", 0)
        if (characterId != 0) {
            getCharacterDetails(characterId)
        } else {
            showError()
        }
    }

    private fun getCharacterDetails(characterId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitProvider.getRetrofit().create(APIService::class.java).getCharacter(characterId)
            val character = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    val episodeList = character?.episode ?: emptyList()
                    val episodeNumbers = episodeList.map { url ->
                        url.substringAfterLast("/").toIntOrNull()
                    }.filterNotNull().joinToString(", ")

                    Picasso.get().load(character?.image).into(characterImage)
                    characterName.text = character?.name
                    characterStatus.text = "[\uD83D\uDFE2]  Status: ${character?.status}"
                    characterSpecies.text = "[\uD83D\uDC3E] Species: ${character?.species}"
                    characterType.text = "[\uD83C\uDFF7️] Type: ${character?.type}"
                    characterGender.text = "[♀️] Gender: ${character?.gender}"
                    characterOriginName.text = "[\uD83C\uDFE0] Origin: ${character?.origin?.name}"
                    characterLocationName.text = "[\uD83D\uDCCD] Location: ${character?.location?.name}"
                    characterEpisode.text = "[\uD83C\uDFAC] Episodes: $episodeNumbers"
                } else {
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Nothing found", Toast.LENGTH_SHORT).show()
    }
}