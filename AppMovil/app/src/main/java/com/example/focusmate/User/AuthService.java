package com.example.focusmate.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.focusmate.RetrofitClient;
import com.google.gson.Gson;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthService {
    private static final String TAG = "AuthService";
    private static final String PREFS_NAME = "FocusMatePrefs";
    private static final String KEY_USER_DATA = "user_data";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    
    private CompositeDisposable disposables = new CompositeDisposable();
    private Context context;
    
    public interface AuthCallback {
        void onLoginSuccess(User user);
        void onLoginError(String error);
    }
    
    public AuthService(Context context) {
        this.context = context;
    }
    
    public void login(String username, String password, AuthCallback callback) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        
        disposables.add(
            RetrofitClient.getApiService()
                .login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        if (user.getError() == null) {
                            saveUserData(user);
                            callback.onLoginSuccess(user);
                        } else {
                            callback.onLoginError(user.getError());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error en login", e);
                        callback.onLoginError("Error de conexión: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Login request completed");
                    }
                })
        );
    }
    
    private void saveUserData(User user) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        
        editor.putString(KEY_USER_DATA, userJson);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }
    
    public User getUserData() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userJson = prefs.getString(KEY_USER_DATA, null);
        
        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }
    
    public boolean isLoggedIn() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public void logout() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_USER_DATA);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }
    
    public void destroy() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
