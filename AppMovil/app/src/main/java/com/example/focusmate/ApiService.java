package com.example.focusmate;

import com.example.focusmate.Achievement.Achievement;
import com.example.focusmate.Session.Session;
import com.example.focusmate.StudyMethods.StudyMethodResponse;
import com.example.focusmate.User.LoginRequest;
import com.example.focusmate.User.User;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("usuarios/comprobar")
    Observable<User> login(@Body LoginRequest loginRequest);

    @GET("usuario/sesiones")
    Observable<List<Session>> getUserSessions(@Query("user_id") int user_id);
    
    @POST("usuario/sesiones/agregar")
    Observable<String> createStudySession(@Body Session session);

    @GET("metodos")
    Observable<StudyMethodResponse> getStudyMethods();

    @GET("logros")
    Observable<List<Achievement>> getAllAchievements();

    @GET("usuario/logros")
    Observable<List<Achievement>> getUserAchievements(@Query("user_id") int user_id);


}