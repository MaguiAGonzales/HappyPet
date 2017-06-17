package app.vidas.salvando.com.cajamarca.salvandovidas.agents;

public class AgentUtils {

    private AgentUtils() {}

    public static final String BASE_URL = "http://api.karinacabrera.idesoft.co/api/";

    public static <A> A getAgent(Class<A> agentClass) {
        return RetrofitClient.getClient(BASE_URL)
                .create(agentClass);
    }
}
