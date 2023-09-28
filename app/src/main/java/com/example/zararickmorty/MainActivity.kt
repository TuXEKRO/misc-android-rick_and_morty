package com.example.zararickmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zararickmorty.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnQueryTextListener, FilterDialogFragment.FilterDialogListener {
    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter:CharacterAdapter
    private val filters = mutableMapOf<String, String>()
    private val charactersList = mutableListOf<com.example.zararickmorty.Character>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        searchByFilters(emptyMap())
        screenSplash.setKeepOnScreenCondition { false }
    }

    private fun initRecyclerView() {
        adapter = CharacterAdapter(charactersList)
        binding.rvCharacters.layoutManager = LinearLayoutManager(this)
        binding.rvCharacters.adapter = adapter
    }

    private fun searchByFilters(query:Map<String, String>) {
        binding.progressBar.visibility = View.VISIBLE
        println("Se va a llamar a searchByFilters con: ")
        println(query)
        println(filters)
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitProvider.getRetrofit().create(APIService::class.java).getCharacters(query)
            val characters = call.body()
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
                if (call.isSuccessful) {
                    val charactersReceived = characters?.results ?: emptyList()
                    charactersList.clear()
                    charactersList.addAll(charactersReceived)
                    adapter.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Nothing found", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            filters["name"] = query
        }
        searchByFilters(filters)
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            filters["name"] = query
        }
        searchByFilters(filters)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    // Show chips filter
    override fun onFilterSelected(status: String, species: String, gender: String) {
        println("$status, $species, $gender")
        if (status.isEmpty() || status == "null") {
            filters.remove("status")
        } else {
            filters["status"] = status
        }

        if (species.isEmpty() || species == "null") {
            filters.remove("species")
        } else {
            filters["species"] = species
        }

        if (gender.isEmpty() || gender == "null") {
            filters.remove("gender")
        } else {
            filters["gender"] = gender
        }
        searchByFilters(filters)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val dialog = FilterDialogFragment()
                dialog.listener = this
                dialog.setChipStates(filters)
                dialog.show(supportFragmentManager, "FilterDialogFragment")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}