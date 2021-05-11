package ca.qc.johnabbott.cs616.healthhaven.networking;

public interface OnResponseListener<T> {
    void onResponse(T data);
}
