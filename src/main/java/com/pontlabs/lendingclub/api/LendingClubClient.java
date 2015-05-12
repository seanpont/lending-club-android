package com.pontlabs.lendingclub.api;

import com.google.gson.Gson;
import com.pontlabs.lendingclub.BuildConfig;
import com.pontlabs.lendingclub.LendingClubData;
import com.pontlabs.lendingclub.utils.ThreadUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

import static com.pontlabs.lendingclub.utils.ObjectUtils.weak;

@Singleton
public class LendingClubClient {

  public static final String BASE_URL = "https://api.lendingclub.com/api/investor/v1/accounts/%d/";
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  @Inject OkHttpClient mOkHttpClient;
  @Inject LendingClubData mData;
  @Inject Gson mGson;
  @Inject ThreadUtils mThreadUtils;
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
  public interface ClubCallback<T> {
    void onSuccess(T response);
    void onFailure(String message);
  }

  private <T> void callOnSuccess(final WeakReference<ClubCallback<T>> weakCallback, final T responseVal) {
    mThreadUtils.onMainThread(new Runnable() {
      @Override public void run() {
        ClubCallback<T> callback = weakCallback.get();
        if (callback != null) {
          callback.onSuccess(responseVal);
        }
      }
    });
  }

  private <T> void callOnFailure(final WeakReference<ClubCallback<T>> weakCallback, final String message) {
    mThreadUtils.onMainThread(new Runnable() {
      @Override public void run() {
        ClubCallback<T> callback = weakCallback.get();
        if (callback != null) {
          callback.onFailure(message);
        }
      }
    });
  }

  /**
   * Wrap the callback in a weak reference
   */
  private <T> Callback httpCallback(final Class<T> responseType, ClubCallback<T> callback) {
    final WeakReference<ClubCallback<T>> weakCallback = weak(callback);
    return new Callback() {
      @Override public void onFailure(Request request, IOException e) {
        callOnFailure(weakCallback, e.getMessage());
      }

      @Override public void onResponse(Response response) throws IOException {
        if (response.isSuccessful()) {
          final T responseVal;
          if (BuildConfig.DEBUG) {
            final String json = response.body().string();
            responseVal = mGson.fromJson(json, responseType);
          } else {
            responseVal = mGson.fromJson(response.body().charStream(), responseType);
          }
          callOnSuccess(weakCallback, responseVal);
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

  public void signIn(final int accountId, final String apiKey, final ClubCallback<Summary> callback) {
    final Credentials credentials = Credentials.builder().accountId(accountId).apiKey(apiKey).build();
    ClubCallback<Summary> saveCredentialsOnSuccess = new ClubCallback<Summary>() {
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
    final Callback httpCallback = httpCallback(Summary.class, saveCredentialsOnSuccess);
    final Request request = requestFor("summary", credentials);
    mOkHttpClient.newCall(request).enqueue(httpCallback);
  }

  // ===== API =====================================================================================

  public void getSummary(ClubCallback<Summary> callback) {
    get("summary", Summary.class, callback);
  }

  // ===== HELPERS ===============================================================================

  private <T> void get(String path, Class<T> responseClass, ClubCallback<T> callback) {
    mOkHttpClient.newCall(requestFor(path)).enqueue(httpCallback(responseClass, callback));
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

  // ===== SETTERS ===============================================================================
  // For replacement in tests

  public void setHttpClient(OkHttpClient okHttpClient) {
    mOkHttpClient = okHttpClient;
  }

  public void setThreadUtils(ThreadUtils threadUtils) {
    mThreadUtils = threadUtils;
  }

}
