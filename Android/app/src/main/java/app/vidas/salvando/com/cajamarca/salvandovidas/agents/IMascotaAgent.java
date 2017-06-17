package app.vidas.salvando.com.cajamarca.salvandovidas.agents;


import java.util.List;

import app.vidas.salvando.com.cajamarca.salvandovidas.entities.Mascota;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface IMascotaAgent {

    @GET("mascotas/")
    Observable<List<Mascota>> getMascotas();

    @GET("mascota/{id}")
    Observable<Mascota> getMascota(@Path("id") Integer mascotaId);

    @GET("mis-mascotas")
    Observable<List<Mascota>> getMisMascotas(@Query("user_id") Integer userId);
}
