package com.seguras.laboratorio2.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.seguras.laboratorio2.databinding.RestaurantCardBinding
import com.seguras.laboratorio2.model.Restaurante
import com.squareup.picasso.Picasso

class RestauranteViewHolder(view: View): RecyclerView.ViewHolder(view)/*, View.OnClickListener*/ {
    private val binding = RestaurantCardBinding.bind(view)

    fun bind(item: Restaurante){
        with(binding){
            tvName.text = item.nombre
            tvCalificacion.text = "Calificación: "+item.calificacion
            tvCosto.text = "Costo promedio: "+item.costo
            tvFundacion.text = "Fundado desde "+item.ano
            Picasso.get().load(item.fotos[0]).into(ivThum)
        }
    }
}