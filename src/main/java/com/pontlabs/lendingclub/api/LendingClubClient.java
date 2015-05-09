package com.pontlabs.lendingclub.api;

import android.os.Handler;

import com.google.gson.Gson;
import com.pontlabs.lendingclub.LendingClubData;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Reader;
import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

import static com.pontlabs.lendingclub.utils.ObjectUtils.weak;

@Singleton
public class LendingClubClient {

  private static final String BASE_URL = "https://api.lendingclub.com/api/investor/v1/accounts/%s/";
  @Inject OkHttpClient mOkHttpClient;
  @Inject LendingClubData mData;
  @Inject Gson mGson;
  @Inject Handler mHandler;
  private Credentials mCredentials;

  @Inject LendingClubClient() {}

  public boolean hasCredentials() {
    if (mCredentials == null) {
      mCredentials = mData.getCredentials();
    }
    return mCredentials != null;
  }

  // ===== CALLBACK ================================================================================

  public static abstract class LCCallback<T> {
    final Class<T> mResponseType;
    boolean enabled = true;
    public LCCallback(Class<T> responseType) { mResponseType = responseType; }
    public abstract void onSuccess(T response);
    public abstract void onFailure(String message);
    public void disable() { enabled = false; }
    public void enable() { enabled = true; }
  }

  private <T> void callOnSuccess(final WeakReference<LCCallback<T>> weakCallback, final Reader response) {
    mHandler.post(new Runnable() {
      @Override public void run() {
        LCCallback<T> callback = weakCallback.get();
        if (callback != null && callback.enabled) {
          callback.onSuccess(mGson.fromJson(response, callback.mResponseType));
        }
      }
    });
  }

  private <T> void callOnFailure(final WeakReference<LCCallback<T>> weakCallback, final String message) {
    mHandler.post(new Runnable() {
      @Override public void run() {
        LCCallback<T> callback = weakCallback.get();
        if (callback != null && callback.enabled) {
          callback.onFailure(message);
        }
      }
    });
  }

  // ===== API =====================================================================================

  public void signIn(final int accountId, final String apiKey, LCCallback<Summary> callback) {
    Timber.i("signIn %s %s", accountId, apiKey);
    final WeakReference<LCCallback<Summary>> weakCallback = weak(callback);
    final Credentials credentials = Credentials.builder().accountId(accountId).apiKey(apiKey).build();

    mOkHttpClient.newCall(requestFor("summary", credentials)).enqueue(new Callback() {
      @Override public void onFailure(Request request, IOException e) {
        callOnFailure(weakCallback, e.getMessage());
      }

      @Override public void onResponse(Response response) throws IOException {
        if (response.isSuccessful()) {
          mCredentials = credentials;
          mData.saveCredentials(mCredentials);
          callOnSuccess(weakCallback, response.body().charStream());
        } else {
          callOnFailure(weakCallback, response.body().string());
        }
      }
    });
  }

  public void getSummary(LCCallback<Summary> callback) {
    mOkHttpClient.newCall(requestFor("summary")).enqueue(callbackWrapper(callback));
  }

  // ===== HELPERS ===============================================================================

  /**
   * The callback is assumed to be attached to an activity, so
   * @param callback
   * @param <T>
   * @return
   */
  private <T> Callback callbackWrapper(LCCallback<T> callback) {
    final WeakReference<LCCallback<T>> weakCallback = weak(callback);
    return new Callback() {
      @Override public void onFailure(Request request, IOException e) {
        callOnFailure(weakCallback, e.getMessage());
      }

      @Override public void onResponse(Response response) throws IOException {
        if (response.isSuccessful()) {
          callOnSuccess(weakCallback, response.body().charStream());
        } else {
          callOnFailure(weakCallback, response.body().string());
        }
      }
    };
  }

  private Request requestFor(String path) {
    return requestFor(path, mCredentials);
  }

  private Request requestFor(String path, Credentials credentials) {
    return new Request.Builder()
        .get()
        .url(baseUrl(credentials.accountId()) + path)
        .addHeader("Content-type", "application/json")
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", credentials.apiKey())
        .build();
  }

  private String baseUrl(int accountId) {
    return String.format(BASE_URL, accountId);
  }

  public void setOkHttpClient(OkHttpClient okHttpClient) {
    mOkHttpClient = okHttpClient;
  }
}
