package parseadores;


import android.util.JsonReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import clases.AdopcionMascota;
import clases.Mascota;

public class JsonAdopcionMascotaParser {
    public List<AdopcionMascota> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.setLenient(true);
        System.out.println("LEEDOR ---------->      " + reader.toString());
        try {
            // Leer Array
            return leerArrayMascotas(reader);
        } finally {
            reader.close();
        }

    }

    public List<AdopcionMascota> leerArrayMascotas(JsonReader reader) throws IOException {
        // Lista temporal
        ArrayList<AdopcionMascota> mascotas = new ArrayList<>();

        System.out.println("LEER OBJETO ------->  " + reader.toString());


        reader.beginArray();
        while (reader.hasNext()) {
            // Leer objeto
            mascotas.add(leerMascota(reader));
        }
        reader.endArray();
        return mascotas;
    }

    public AdopcionMascota leerMascota(JsonReader reader) throws IOException {
        // Variables locales
        int idAdopcion = 0;
        String estado = null;
        int usuarioAdopcionId = 0;

        int mascotaId = 0;
        String nombre = null;
        String tipo = null;
        String sexo = null;
        String particularidades = null;
        String salud = null;
        String anio = null;
        String adoptado = null;
        String imagen = null;
        String origen = null;
        Integer usuarioMascotaId = 0;

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
                    idAdopcion = Integer.parseInt(reader.nextString());
                    break;
                case "estado":
                    estado = reader.nextString();
                    break;
                case "usuario_adopcion_id":
                    usuarioAdopcionId = Integer.parseInt(reader.nextString());
                    break;
                case "mascota_id":
                    mascotaId = Integer.parseInt(reader.nextString());
                    break;
                case "nombre":
                    nombre = reader.nextString();
                    break;
                case "tipo_mascota":
                    tipo = reader.nextString();
                    break;
                case "sexo":
                    sexo = reader.nextString();
                    break;
                case "particularidades":
                    particularidades = reader.nextString();
                    break;
                case "salud":
                    salud = reader.nextString();
                    break;
                case "ano_nacimiento":
                    anio = reader.nextString();
                    break;
                case "usuario_mascota_id":
                    usuarioMascotaId = Integer.parseInt(reader.nextString());
                    break;
                case "imagen":
                    imagen = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
//        System.out.println(nombre);
        return new AdopcionMascota(idAdopcion, estado, usuarioAdopcionId, mascotaId,nombre,tipo,sexo,particularidades,salud,anio,imagen,usuarioMascotaId);
    }
}
