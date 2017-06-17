package app.vidas.salvando.com.cajamarca.salvandovidas.agents;

import app.vidas.salvando.com.cajamarca.salvandovidas.entities.Usuario;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface IUserAgent {

    @POST("login")
    Observable<Usuario> login(@Body Usuario usuario);
}
