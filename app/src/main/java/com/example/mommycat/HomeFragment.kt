package com.example.mommycat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var catAdapter: CatAdapter
    private lateinit var database: AppDatabase
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewCats)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        catAdapter = CatAdapter()
        recyclerView.adapter = catAdapter
        database = AppDatabase.getDatabase(requireContext())
        requestQueue = Volley.newRequestQueue(requireContext())

        fetchCatData()

        return view
    }

    private fun fetchCatData() {
        val url = "https://api.npoint.io/3fa9a95557f89f097063"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                if (isAdded) {
                    lifecycleScope.launch {
                        storeCatsData(response)
                        loadCatsFromDatabase()
                    }
                }
            },
            Response.ErrorListener { error ->
                if (isAdded) {
                    error.printStackTrace()
                    Toast.makeText(requireContext(), "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private suspend fun storeCatsData(response: JSONArray) {
        withContext(Dispatchers.IO) {
            for (i in 0 until response.length()) {
                val catObject = response.getJSONObject(i)
                val cat = Cat(
                    id = catObject.getString("CatID"),
                    name = catObject.getString("CatName"),
                    description = catObject.getString("CatDescription"),
                    price = catObject.getDouble("CatPrice"),
                    imageUrl = catObject.getString("CatImage")
                )
                database.catDao().insert(cat)
            }
        }
    }

    private suspend fun loadCatsFromDatabase() {
        val cats = withContext(Dispatchers.IO) {
            database.catDao().getAllCats()
        }
        if (isAdded) {
            catAdapter.submitList(cats)
        }
    }
}
