package com.example.focusmate.Session;

import android.util.Log;

import com.example.focusmate.RetrofitClient;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SessionManager {
    private static final String TAG = "SessionManager";
    private CompositeDisposable disposables = new CompositeDisposable();
    private SessionCallback callback;

    public interface SessionCallback {
        void onSessionCreated(SessionResponse response);
        void onSessionError(String error);
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
                                    // Crear un objeto SessionResponse simple
                                    SessionResponse sessionResponse = new SessionResponse();
                                    sessionResponse.setMessage(response);
                                    sessionResponse.setSuccess(true);
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
    public void destroy() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}