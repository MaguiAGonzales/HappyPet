package clases;


import android.icu.util.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mascota {
    private int idMascota;
    private String nombre;
    private String tipo;
    private String sexo;
    private String particularidades;
    private String salud;
    private String anio;
    private String adoptado;
    private String imagen;
    private String origen;
    private int usuarioId;


    public Mascota(int idMascota, String nombre, String tipo, String sexo, String particularidades, String salud, String anio, String adoptado, String imagen, String origen, int usuarioId) {
        this.idMascota = idMascota;
        this.nombre = nombre;
        this.tipo = tipo;
        this.sexo = sexo;
        this.particularidades = particularidades;
        this.salud = salud;
        this.anio = anio;
        this.adoptado = adoptado;
        this.imagen = imagen;
        this.origen = origen;
        this.usuarioId = usuarioId;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getAdoptado() {
        return adoptado;
    }

    public void setAdoptado(String adoptado) {
        this.adoptado = adoptado;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getEdad(){
        int anioActual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())) ;
//        Calendar calendar = Calendar.getInstance();
//        int anioActual = calendar.get(Calendar.YEAR);
        return anioActual - Integer.parseInt(this.anio);
    }
}
