package clases;

public class Adopcion {
    private int idAdopcion;
    private String estado;
    private String terminos_condiciones;
    private int id_horario;
    private int id_mascota;
    private int id_usuario;
    private int id_test;
    private int id_visita_adopcion;

    private Mascota mascota;

    public Adopcion(int idAdopcion, String estado, String terminos_condiciones, int id_horario, int id_mascota, int id_usuario, int id_test, int id_visita_adopcion) {
        this.idAdopcion = idAdopcion;
        this.estado = estado;
        this.terminos_condiciones = terminos_condiciones;
        this.id_horario = id_horario;
        this.id_mascota = id_mascota;
        this.id_usuario = id_usuario;
        this.id_test = id_test;
        this.id_visita_adopcion = id_visita_adopcion;
    }

    public Adopcion(int idAdopcion, String estado, String terminos_condiciones, int id_horario, int id_mascota, int id_usuario, int id_test, int id_visita_adopcion, Mascota mascota) {
        this.idAdopcion = idAdopcion;
        this.estado = estado;
        this.terminos_condiciones = terminos_condiciones;
        this.id_horario = id_horario;
        this.id_mascota = id_mascota;
        this.id_usuario = id_usuario;
        this.id_test = id_test;
        this.id_visita_adopcion = id_visita_adopcion;
        this.mascota = mascota;
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

    public String getTerminos_condiciones() {
        return terminos_condiciones;
    }

    public void setTerminos_condiciones(String terminos_condiciones) {
        this.terminos_condiciones = terminos_condiciones;
    }

    public int getId_horario() {
        return id_horario;
    }

    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    public int getId_mascota() {
        return id_mascota;
    }

    public void setId_mascota(int id_mascota) {
        this.id_mascota = id_mascota;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_test() {
        return id_test;
    }

    public void setId_test(int id_test) {
        this.id_test = id_test;
    }

    public int getId_visita_adopcion() {
        return id_visita_adopcion;
    }

    public void setId_visita_adopcion(int id_visita_adopcion) {
        this.id_visita_adopcion = id_visita_adopcion;
    }


    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}
