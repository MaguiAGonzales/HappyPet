package app.vidas.salvando.com.cajamarca.salvandovidas.entities;

import com.google.gson.annotations.SerializedName;

public class Mascota {

    @SerializedName("id")
    private Integer mascotaId;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("tipo_mascota")
    private String tipoMascota;

    @SerializedName("sexo")
    private String sexo;

    @SerializedName("foto")
    private String foto;

    @SerializedName("particularidades")
    private String particularidades;

    @SerializedName("salud")
    private String salud;

    @SerializedName("tamano")
    private String tamano;

    @SerializedName("ano_nacimiento")
    private String anoNacimiento;

    @SerializedName("created_at")
    private String fechaRegistro;

    @SerializedName("id_usuario")
    private Integer usuarioId;

    public Mascota(Integer mascotaId) {
        this.mascotaId = mascotaId;
    }

    public Integer getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Integer mascotaId) {
        this.mascotaId = mascotaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoMascota() {
        return tipoMascota;
    }

    public void setTipoMascota(String tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getParticularidades() {
        return particularidades;
    }

    public void setParticularidades(String particularidades) {
        this.particularidades = particularidades;
    }

    public String getSalud() {
        return salud;
    }

    public void setSalud(String salud) {
        this.salud = salud;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getAnoNacimiento() {
        return anoNacimiento;
    }

    public void setAnoNacimiento(String anoNacimiento) {
        this.anoNacimiento = anoNacimiento;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
