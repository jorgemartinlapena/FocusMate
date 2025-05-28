package com.example.focusmate.StudyMethods;

import android.util.Log;
import com.example.focusmate.RetrofitClient;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudyMethodManager {
    private static final String TAG = "StudyMethodManager";
    private CompositeDisposable disposables = new CompositeDisposable();

    public interface StudyMethodsCallback {
        void onMethodsLoaded(List<StudyMethod> methods);
        void onError(String error);
    }

    public StudyMethodManager() {
        // Constructor vacío, no necesita callback como parámetro
    }

    public void getStudyMethods(StudyMethodsCallback callback) {
        disposables.add(
                RetrofitClient.getApiService()
                        .getStudyMethods()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<StudyMethodResponse>() {
                            @Override
                            public void onNext(StudyMethodResponse response) {
                                Log.d(TAG, "Métodos obtenidos exitosamente");
                                if (callback != null) {
                                    if (response.getMetodos() != null) {
                                        callback.onMethodsLoaded(response.getMetodos());
                                    } else {
                                        callback.onError("No se encontraron métodos");
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                String errorMsg = "Error al obtener métodos: " + e.getMessage();
                                Log.e(TAG, errorMsg, e);
                                if (callback != null) {
                                    callback.onError(errorMsg);
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Petición de métodos completada");
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