package clases;


import java.text.SimpleDateFormat;
import java.util.Date;

public class AdopcionMascota {
    private int idAdopcion;
    private String estado;
    private int usuarioAdopcionId;

    private int mascotaId;
    private String nombre;
    private String tipo;
    private String sexo;
    private String particularidades;
    private String salud;
    private String anio;
    private String imagen;
    private int usuarioMascotaId;

    public AdopcionMascota(int idAdopcion, String estado, int usuarioAdopcionId, int mascotaId, String nombre, String tipo, String sexo, String particularidades, String salud, String anio, String imagen, int usuarioMascotaId) {
        this.idAdopcion = idAdopcion;
        this.estado = estado;
        this.usuarioAdopcionId = usuarioAdopcionId;

        this.mascotaId = mascotaId;
        this.nombre = nombre;
        this.tipo = tipo;
        this.sexo = sexo;
        this.particularidades = particularidades;
        this.salud = salud;
        this.anio = anio;
        this.imagen = imagen;
        this.usuarioMascotaId = usuarioMascotaId;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(int mascotaId) {
        this.mascotaId = mascotaId;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


    public int getUsuarioMascotaId() {
        return usuarioMascotaId;
    }

    public void setUsuarioMascotaId(int usuarioMascotaId) {
        this.usuarioMascotaId = usuarioMascotaId;
    }

    public int getEdad(){
        int anioActual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())) ;
//        Calendar calendar = Calendar.getInstance();
//        int anioActual = calendar.get(Calendar.YEAR);
        return anioActual - Integer.parseInt(this.anio);
    }



    public int getIdAdopcion() {
        return idAdopcion;
    }

    public void setIdAdopcion(int idAdopcion) {
        this.idAdopcion = idAdopcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getUsuarioAdopcionId() {
        return usuarioAdopcionId;
    }

    public void setUsuarioAdopcionId(int usuarioAdopcionId) {
        this.usuarioAdopcionId = usuarioAdopcionId;
    }
}
