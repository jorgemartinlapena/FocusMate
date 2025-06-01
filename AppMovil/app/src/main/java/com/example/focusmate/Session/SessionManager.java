package com.example.focusmate.Session;

import android.util.Log;

import com.example.focusmate.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SessionManager {
    private static final String TAG = "SessionManager";
    private CompositeDisposable disposables = new CompositeDisposable();
    private SessionCallback callback;

    public interface SessionCallback {
        void onSessionCreated(SessionPostResponse response);
        void onSessionError(String error);
        void onSessionsLoaded(List<Session> sessions);
    }

    public SessionManager(SessionCallback callback) {
        this.callback = callback;
    }

    public void createStudySession(int userId, int methodId, int durationMinutes,
                                   String taskType, int productivityLevel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());

        Session session = new Session(
                userId, methodId, timestamp, durationMinutes, taskType,
                productivityLevel
        );

        disposables.add(
                RetrofitClient.getApiService()
                        .createStudySession(session)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<String>() {
                            @Override
                            public void onNext(String response) {
                                Log.d(TAG, "Sesión creada exitosamente: " + response);
                                if (callback != null) {
                                    SessionPostResponse sessionResponse = new SessionPostResponse(
                                            "Sesión creada exitosamente",
                                            true
                                    );
                                    callback.onSessionCreated(sessionResponse);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                String errorMsg = "Error al crear sesión: " + e.getMessage();
                                Log.e(TAG, errorMsg, e);
                                if (callback != null) {
                                    callback.onSessionError(errorMsg);
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Petición completada");
                            }
                        })
        );
    }


    public void getUserSessions(int userId) {
        disposables.add(
                RetrofitClient.getApiService()
                        .getUserSessions(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Session>>() {
                            @Override
                            public void onNext(List<Session> sessions) {
                                Log.d(TAG, "Sesiones obtenidas exitosamente: " + sessions.size() + " sesiones");
                                if (callback != null) {
                                    callback.onSessionsLoaded(sessions);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                String errorMsg = "Error al obtener sesiones: " + e.getMessage();
                                Log.e(TAG, errorMsg, e);
                                if (callback != null) {
                                    callback.onSessionError(errorMsg);
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Petición de sesiones completada");
                            }
                        })
        );
    }
    public void destroy() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}