package com.example.rxjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.example.rxjava.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Function;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        final ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setLifecycleOwner(this);

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Object> emitter) throws Throwable {
                   activityMainBinding.editText.addTextChangedListener(new TextWatcher() {
                       @Override
                       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                       }

                       @Override
                       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                               if (charSequence.length()!=0)
                               emitter.onNext(charSequence);
                       }

                       @Override
                       public void afterTextChanged(Editable editable) {

                       }
                   });
            }
        })
                .doOnNext(c-> Log.d(TAG, "shimaa upStream: "+c))
                /*.map(new Function<Object, Object>() {
                    @Override
                    public Object apply(Object o) throws Throwable {
                        return Integer.parseInt(o.toString())*2;
                    }
                })*/
                .filter(c-> !c.toString().equals("shimaa"))
                /*.debounce(2, TimeUnit.SECONDS)
                .distinctUntilChanged()*/
                .subscribe(s-> {
                    Log.d(TAG, "shimaa downStream: " + s);
                    sendDataToAPI(s.toString());
                });
    }

    public Observable sendDataToAPI(String data)
    {
        Observable observable=Observable.just("calling API 1 to send "+data);
        observable.subscribe(c-> Log.d(TAG, "shimaa sendDataToAPI: "+c));
        return observable;
    }
}
