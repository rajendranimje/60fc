package in.gov.cgg.alumni.trial.fcmMine;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import in.gov.cgg.alumni.GlobalDeclaration;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).
                addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder requestBuilder = chain.request().newBuilder();
                        requestBuilder.header("Content-Type", "application/json");
                        requestBuilder.header("Accept", "application/json");
                        requestBuilder.header("Authorization", GlobalDeclaration.MYKEY);
                        return chain.proceed(requestBuilder.build());
                    }
                });

        OkHttpClient httpClient = httpClientBuilder.build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalDeclaration.BASEURL).client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

        }
        return retrofit;
    }

}