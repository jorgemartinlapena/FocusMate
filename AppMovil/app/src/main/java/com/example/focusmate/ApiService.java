package com.example.focusmate;

import com.example.focusmate.Session.Session;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {

    @POST("usuario/sesiones/agregar")
    Observable<String> createStudySession(@Body Session session);

}