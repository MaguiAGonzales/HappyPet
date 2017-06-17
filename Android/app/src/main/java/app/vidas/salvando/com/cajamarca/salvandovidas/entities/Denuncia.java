package app.vidas.salvando.com.cajamarca.salvandovidas.entities;

import com.google.gson.annotations.SerializedName;

public class Denuncia {

    @SerializedName("id")
    private Integer denunciaId;

    @SerializedName("tipo_denuncia")
    private String tipoDenuncia;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("foto")
    private String foto;

    @SerializedName("created_at")
    private String fechaCreacion;
}
