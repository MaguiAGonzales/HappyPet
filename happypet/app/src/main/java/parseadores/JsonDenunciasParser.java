package parseadores;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import clases.Denuncia;

public class JsonDenunciasParser {
    public List<Denuncia> leerFlujoJson(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.setLenient(true);
        try {
            return leerArrayDenuncias(reader);
        } finally {
            reader.close();
        }

    }

    public List<Denuncia> leerArrayDenuncias(JsonReader reader) throws IOException {
        ArrayList<Denuncia> denuncia = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            denuncia.add(leerDenuncia(reader));
        }
        reader.endArray();
        return denuncia;
    }

    public Denuncia leerDenuncia(JsonReader reader) throws IOException {
        // Variables locales
        int idDenuncia = 0;
        String tipo = null;
        String fecha = null;
        String titulo = null;
        String descripcion = null;
        String foto = null;
        String telefono = null;
        Boolean estado = null;
        Integer usuarioId = 0;

        reader.beginObject();

        while (reader.hasNext()) {

            String name = reader.nextName();
            switch (name) {
                case "id":
                    idDenuncia = Integer.parseInt(reader.nextString());
                    break;
                case "tipo":
                    tipo = reader.nextString();
                    break;
                case "fecha":
                    String fechaVisita = reader.nextString();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date fechaInicio = format.parse(fechaVisita);
                        format = new SimpleDateFormat("dd/MM/yyyy");
                        fechaVisita = format.format(fechaInicio);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    fecha = fechaVisita;
                    break;
                case "titulo":
                    titulo = reader.nextString();
                    break;
                case "descripcion":
                    descripcion = reader.nextString();
                    break;
                case "foto":
                    foto = reader.nextString();
                    break;
                case "telefono":
                    telefono = reader.nextString();
                    break;
                case "estado":
                    estado = reader.nextString() == "1" ? true : false;
                    break;
                case "id_usuario":
                    usuarioId = Integer.parseInt(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Denuncia(idDenuncia, tipo, fecha, titulo, descripcion, foto, telefono, estado, usuarioId);
    }
}
