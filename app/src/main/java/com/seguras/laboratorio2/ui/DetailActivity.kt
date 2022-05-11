package com.seguras.laboratorio2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.seguras.laboratorio2.databinding.ActivityDetailBinding
import com.seguras.laboratorio2.model.Restaurante
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val restaurante = bundle?.getSerializable("selecRest") as Restaurante

        with(binding){
            tvDname.text = restaurante.nombre
            tvDcalificacion.text = "Calificación: " + restaurante.calificacion
            tvDcosto.text = "Costo Promedio: " + restaurante.costo
            tvDfundacion.text = "Fundando desde " + restaurante.ano
            tvResena.text = restaurante.resena
            tvDireccion.text = "Dirección:\n"+restaurante.direccion
            Picasso.get().load(restaurante.fotos[0]).into(ivUno)
            Picasso.get().load(restaurante.fotos[1]).into(ivDos)
            Picasso.get().load(restaurante.fotos[2]).into(ivTres)
            Picasso.get().load(restaurante.fotos[3]).into(ivCuatro)
        }
    }
}