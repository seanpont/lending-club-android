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
  Credentials mCredentials;

  @Inject LendingClubClient() {}

  // ===== CALLBACK ================================================================================

  /**
   * Callback to the UI thread that initiated the request.
   * onSuccess and onFailure are called on the main thread.
   * The callback will only be made if it is enabled. This allows an activity to disable
   * a callback when it is paused but before it has been reclaimed.
   * The callback is stored in a weak reference so the activity can be reclaimed.
   * @param <T> the type to be returned.
   */
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

  /**
   * Wrap the callback in a weak reference
   */
  private <T> Callback httpCallback(LCCallback<T> callback) {
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

  // ===== AUTHENTICATION ==========================================================================

  public boolean hasCredentials() {
    if (mCredentials == null) {
      mCredentials = mData.getCredentials();
    }
    return mCredentials != null;
  }

  public void signIn(final int accountId, final String apiKey, final LCCallback<Summary> callback) {
    final Credentials credentials = Credentials.builder().accountId(accountId).apiKey(apiKey).build();
    LCCallback<Summary> saveCredentialsOnSuccess = new LCCallback<Summary>(Summary.class) {
      @Override public void onSuccess(Summary response) {
        mCredentials = credentials;
        mData.saveCredentials(mCredentials);
        callback.onSuccess(response);
      }

      @Override public void onFailure(String message) {
        callback.onFailure(message);
      }
    };

    Timber.i("signIn %s %s", accountId, apiKey);
    mOkHttpClient.newCall(requestFor("summary", credentials)).enqueue(httpCallback(saveCredentialsOnSuccess));
  }

  // ===== API =====================================================================================

  public void getSummary(LCCallback<Summary> callback) {
    mOkHttpClient.newCall(requestFor("summary")).enqueue(httpCallback(callback));
  }

  // ===== HELPERS ===============================================================================

  private <T> void get(String path, LCCallback<T> callback) {
    mOkHttpClient.newCall(requestFor(path)).enqueue(httpCallback(callback));
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
