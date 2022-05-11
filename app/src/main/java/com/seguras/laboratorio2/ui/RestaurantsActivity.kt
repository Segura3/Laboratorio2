package com.seguras.laboratorio2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.seguras.laboratorio2.adapter.OnRestClickListener
import com.seguras.laboratorio2.adapter.RestauranteAdapter
import com.seguras.laboratorio2.databinding.ActivityRestaurantsBinding
import com.seguras.laboratorio2.model.Restaurante
import com.seguras.laboratorio2.services.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantsActivity : AppCompatActivity(), OnRestClickListener {

    private lateinit var binding: ActivityRestaurantsBinding
    private lateinit var adapter: RestauranteAdapter
    private var restaurantes = ArrayList<Restaurante>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        loadRestaurantes()

    }

    private fun initRecyclerView(){
        adapter = RestauranteAdapter(restaurantes, this)
        binding.rvRestaurantes.layoutManager = LinearLayoutManager(this)
        binding.rvRestaurantes.adapter = adapter
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://demo4802870.mockable.io/restaurantes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadRestaurantes(){
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<ArrayList<Restaurante>> = getRetrofit().create(APIService::class.java).getRestaurantes("getlist")
            val items: ArrayList<Restaurante>? = call.body()
            runOnUiThread {
                if(call.isSuccessful){
                    restaurantes.clear()
                    restaurantes.addAll(items!!)
                    adapter.notifyDataSetChanged()
                }else{
                    showError()
                }
            }
        }
    }

    private fun showError(){
        Toast.makeText(this, "Error en la carga de datos", Toast.LENGTH_LONG).show()
    }

    override fun onRestItemClicked(position: Int) {
        //Toast.makeText(this, "Restaurante seleccionado: "+restaurantes[position].nombre, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, DetailActivity::class.java)
        val parametros = Bundle()
        parametros.putSerializable("selecRest",restaurantes[position])
        intent.putExtras(parametros)
        startActivity(intent)
    }
}