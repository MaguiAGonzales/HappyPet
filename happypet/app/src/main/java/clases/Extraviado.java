package clases;

public class Extraviado {
    private int idExtraviado;
    private String fecha;
    private String detalle;
    private String encontrado;
    private int idMascota;

    private String imagen;
    private String nombre;
    private String celular;

    public Extraviado(){

    }

    public Extraviado(int idExtraviado, String fecha, String detalle, String encontrado, int idMascota, String imagen, String nombre, String celular) {
        this.idExtraviado = idExtraviado;
        this.fecha = fecha;
        this.detalle = detalle;
        this.encontrado = encontrado;
        this.idMascota = idMascota;
        this.imagen = imagen;
        this.nombre = nombre;
        this.celular = celular;
    }

    public int getIdExtraviado() {
        return idExtraviado;
    }

    public void setIdExtraviado(int idExtraviado) {
        this.idExtraviado = idExtraviado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getEncontrado() {
        return encontrado;
    }

    public void setEncontrado(String encontrado) {
        this.encontrado = encontrado;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
