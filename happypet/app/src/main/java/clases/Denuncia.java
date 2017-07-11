package clases;


public class Denuncia {
    private int idDenuncia;
    private String tipo;
    private String fecha;
    private String titulo;
    private String descripcion;
    private String foto;
    private String telefono;
    private Boolean estado;
    private int usuarioId;

    public Denuncia(){

    }

    public Denuncia(int idDenuncia, String tipo, String fecha, String titulo, String descripcion, String foto, String telefono, Boolean estado, int usuarioId) {
        this.idDenuncia = idDenuncia;
        this.tipo = tipo;
        this.fecha = fecha;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.foto = foto;
        this.telefono = telefono;
        this.estado = estado;
        this.usuarioId = usuarioId;
    }

    public int getIdDenuncia() {
        return idDenuncia;
    }

    public void setIdDenuncia(int idDenuncia) {
        this.idDenuncia = idDenuncia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}
