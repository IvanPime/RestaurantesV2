package com.diplomado.firebase.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Resenia(
        @SerializedName("nombre_usuario")
        var nombreUsuario: String,
        var resenia: String
    ) : Parcelable
