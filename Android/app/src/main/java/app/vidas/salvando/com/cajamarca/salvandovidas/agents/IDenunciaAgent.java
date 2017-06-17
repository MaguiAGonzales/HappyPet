package app.vidas.salvando.com.cajamarca.salvandovidas.agents;

import java.util.List;

import app.vidas.salvando.com.cajamarca.salvandovidas.entities.Denuncia;
import retrofit2.http.GET;
import rx.Observable;

public interface IDenunciaAgent {

    @GET("/denuncias")
    Observable<List<Denuncia>> getDenuncias();
}
