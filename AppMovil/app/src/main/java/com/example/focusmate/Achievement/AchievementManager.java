package com.example.focusmate.Achievement;

import android.content.Context;
import android.util.Log;

import com.example.focusmate.RetrofitClient;
import com.example.focusmate.User.AuthService;
import com.example.focusmate.User.User;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AchievementManager {
    private static final String TAG = "AchievementManager";
    private CompositeDisposable disposables = new CompositeDisposable();
    private AchievementCallback callback;
    private Context context;

    public interface AchievementCallback {
        void onAchievementsLoaded(List<Achievement> achievements);
        void onUserAchievementsLoaded(List<Achievement> achievements);
        void onError(String error);
    }

    public AchievementManager(AchievementCallback callback) {
        this.callback = callback;
    }

    public AchievementManager(AchievementCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
    }

    // Método para obtener el ID del usuario logueado
    private int getCurrentUserId() {
        if (context == null) {
            Log.w(TAG, "Context is null, using default user ID");
            return 1; // Fallback
        }
        
        AuthService authService = new AuthService(context);
        User user = authService.getUserData();
        return user != null ? user.getId() : 1;
    }

    public void getAllAchievements() {
        disposables.add(
                RetrofitClient.getApiService()
                        .getAllAchievements()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Achievement>>() {
                            @Override
                            public void onNext(List<Achievement> achievements) {
                                Log.d(TAG, "Logros obtenidos: " + achievements.size() + " logros");
                                if (callback != null) {
                                    callback.onAchievementsLoaded(achievements);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                String errorMsg = "Error al obtener logros: " + e.getMessage();
                                Log.e(TAG, errorMsg, e);
                                if (callback != null) {
                                    callback.onError(errorMsg);
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Petición de logros completada");
                            }
                        })
        );
    }

    // Obtener logros del usuario específico
    public void getUserAchievements() {
        int userId = getCurrentUserId();
        getUserAchievements(userId);
    }

    public void getUserAchievements(int userId) {
        disposables.add(
                RetrofitClient.getApiService()
                        .getUserAchievements(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Achievement>>() {
                            @Override
                            public void onNext(List<Achievement> achievements) {
                                Log.d(TAG, "Logros del usuario obtenidos: " + achievements.size() + " logros");
                                if (callback != null) {
                                    callback.onUserAchievementsLoaded(achievements);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                String errorMsg = "Error al obtener logros del usuario: " + e.getMessage();
                                Log.e(TAG, errorMsg, e);
                                if (callback != null) {
                                    callback.onError(errorMsg);
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Petición de logros del usuario completada");
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