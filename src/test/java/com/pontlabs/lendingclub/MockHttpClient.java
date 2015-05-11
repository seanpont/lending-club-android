package com.pontlabs.lendingclub;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
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

  }

  public RequestCallback popRequest() {
    assertThat(mRequests.isEmpty(), is(false));
    return new RequestCallback(mRequests.pop(), mCallbacks.pop());
  }
}
