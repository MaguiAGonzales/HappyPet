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

import clases.Extraviado;

public class JsonExtraviadosParser {
    public List<Extraviado> leerFlujoJson(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.setLenient(true);
        try {
            return leerArrayExtraviados(reader);
        } finally {
            reader.close();
        }

    }

    public List<Extraviado> leerArrayExtraviados(JsonReader reader) throws IOException {
        ArrayList<Extraviado> extraviado = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            extraviado.add(leerExtraviado(reader));
        }
        reader.endArray();
        return extraviado;
    }

    public Extraviado leerExtraviado(JsonReader reader) throws IOException {
        // Variables locales
        int idExtraviado = 0;
        String fecha = null;
        String detalle = null;
        String encontrado = null;
        String imagen = null;
        String nombre = null;
        String celular = null;
        Integer mascotaId = 0;

        reader.beginObject();

        while (reader.hasNext()) {

            String name = reader.nextName();
            switch (name) {
                case "id":
                    idExtraviado = Integer.parseInt(reader.nextString());
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
                case "detalle":
                    detalle = reader.nextString();
                    break;
                case "encontrado":
                    encontrado = reader.nextString();
                    break;
                case "nombre":
                    nombre = reader.nextString();
                    break;
                case "imagen":
                    imagen = reader.nextString();
                    break;
                case "celular":
                    celular = reader.nextString();
                    break;
                case "id_mascota":
                    mascotaId = Integer.parseInt(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Extraviado(idExtraviado, fecha,detalle, encontrado, mascotaId, imagen, nombre, celular );
    }
}
