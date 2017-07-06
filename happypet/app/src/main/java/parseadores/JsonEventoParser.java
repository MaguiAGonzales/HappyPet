package parseadores;


import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import clases.Evento;


public class JsonEventoParser {
    public List<Evento> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.setLenient(true);
        System.out.println("LEEDOR ---------->      " + reader.toString());
        try {
            // Leer Array
            return leerArrayEvento(reader);
        } finally {
            reader.close();
        }

    }

    public List<Evento> leerArrayEvento(JsonReader reader) throws IOException {
        // Lista temporal
        ArrayList<Evento> eventos = new ArrayList<>();

        System.out.println("LEER OBJETO ------->  " + reader.toString());


        reader.beginArray();
        while (reader.hasNext()) {
            // Leer objeto
            eventos.add(leerEvento(reader));
        }
        reader.endArray();
        return eventos;
    }

    public Evento leerEvento(JsonReader reader) throws IOException {
        // Variables locales
        int id = 0;
        String titulo = null;
        String descripcion = null;
        String fecha = null;
        String hora = null;
        String lugar = null;
        String referencia = null;

        System.out.println("INICIAR OBJETO --------- " + reader.toString());
        // Iniciar objeto
        reader.beginObject();

        /*
        Lectura de cada atributo
         */
        while (reader.hasNext()) {

            String name = reader.nextName();
            switch (name) {
                case "id":
                    id = Integer.parseInt(reader.nextString());
                    break;
                case "titulo":
                    titulo = reader.nextString();
                    break;
                case "descripcion":
                    descripcion = reader.nextString();
                    break;
                case "fecha":
                    fecha = reader.nextString();
                    break;
                case "hora":
                    hora = reader.nextString();
                    break;
                case "lugar":
                    lugar = reader.nextString();
                    break;
                case "referencia":
                    referencia = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
//        System.out.println(nombre);
        return new Evento(id,titulo,descripcion,fecha,hora,lugar,referencia);
    }
}
