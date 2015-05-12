package com.pontlabs.lendingclub;

import android.content.Context;

import com.pontlabs.lendingclub.api.LendingClubClient;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.robolectric.util.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MockHttpClient extends OkHttpClient {

  final ArrayDeque<Request> mRequests = new ArrayDeque<>();
  final ArrayDeque<Callback> mCallbacks = new ArrayDeque<>();

  @Override public Call newCall(Request request) {
    mRequests.add(request);
    return new MockCall(this, request);
  }

  class MockCall extends Call {
    protected MockCall(OkHttpClient client, Request originalRequest) {
      super(client, originalRequest);
    }

    @Override public Response execute() throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override public void enqueue(Callback responseCallback) {
      mCallbacks.add(responseCallback);
    }
  }

  public static class RequestCallback {
    public final Request mRequest;
    public final Callback mCallback;

    public RequestCallback(Request request, Callback callback) {
      mRequest = request;
      mCallback = callback;
    }

    public void respondWithResource(Context context, int resourceId) throws IOException {
      final InputStream inputStream = context.getResources().openRawResource(resourceId);
      final String json = Strings.fromStream(inputStream);
      Response response = new Response.Builder()
          .request(mRequest)
          .body(ResponseBody.create(LendingClubClient.JSON, json))
          .code(200)
          .protocol(Protocol.HTTP_1_1)
          .build();
      mCallback.onResponse(response);
    }
  }

  public RequestCallback popRequest() {
    assertThat(mRequests.isEmpty(), is(false));
    return new RequestCallback(mRequests.pop(), mCallbacks.pop());
  }
}
