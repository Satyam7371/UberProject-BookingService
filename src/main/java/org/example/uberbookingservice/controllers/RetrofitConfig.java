package org.example.uberbookingservice.controllers;

import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.example.uberbookingservice.apis.LocationServiceApi;
import org.example.uberbookingservice.apis.UberSocketApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Autowired
    private EurekaClient eurekaClient;

    private String getServiceUrl(String serviceName) {
        return eurekaClient.getNextServerFromEureka(serviceName, false).getHomePageUrl();
    }

    @Bean
    public LocationServiceApi locationServiceApi() {
        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("UBERPROJECT-LOCATIONSERVICE"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build().create(LocationServiceApi.class);
    }

    @Bean
    public UberSocketApi uberSocketApi() {
        String serviceUrl = getServiceUrl("UBERSOCKETSERVER");
        System.out.println("Service url for socket" + serviceUrl);

        return new Retrofit.Builder()
                .baseUrl(serviceUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(UberSocketApi.class);
    }


}
