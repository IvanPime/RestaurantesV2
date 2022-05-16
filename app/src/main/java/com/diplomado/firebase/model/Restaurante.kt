package com.diplomado.firebase.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurante(
    @SerializedName("id")
    var idRestaurante: Integer,
    var nombre: String,
    var calificacion: Float,
    var anio: Integer,
    @SerializedName("costo_promedio")
    var costoPromedio: Float,
    var direccion: String,
    var ubicacion: String,
    var latitud: Double,
    var longitud: Double,
    var resenias: List<Resenia>,
    var imagenes: List<String>
) : Parcelable