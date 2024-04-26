package guru.qa.niffler.retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInitializer {

    public Retrofit retrofit;

    public RetrofitInitializer(String host) {
        retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    @SneakyThrows
    public <ResponseClass> ResponseClass executeRequest(Call<ResponseClass> call, Class<ResponseClass> responseType) {
        Response<ResponseClass> response = call.execute();
        assert response.isSuccessful() : """
                Request failed with code: %d %s
                Body = %s
                """ .formatted(response.code(), response.raw(), response.body());
        return response.body() != null ? responseType.cast(response.body()) : null;
    }
    
}
