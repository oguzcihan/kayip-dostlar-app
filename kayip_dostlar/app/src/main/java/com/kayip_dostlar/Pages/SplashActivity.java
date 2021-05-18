package com.kayip_dostlar.Pages;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kayip_dostlar.MainActivity;
import com.kayip_dostlar.R;

public class SplashActivity extends Activity {

    ImageView iv_top , iv_heart , iv_kalpatis , iv_alt ;
    TextView tvheart ;
    CharSequence charSequence;
    int index;
    long delay=5;
    Handler handler =new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iv_top =findViewById(R.id.iv_ust);
        iv_heart=findViewById(R.id.iv_heart);
        iv_kalpatis=findViewById(R.id.iv_kalpatis);
        iv_alt=findViewById(R.id.iv_alt);
        tvheart=findViewById(R.id.tvheart);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation animation1 = AnimationUtils.loadAnimation(this ,R.anim.top_wave );
        iv_top.setAnimation(animation1);

        ObjectAnimator objectAnimator =ObjectAnimator.ofPropertyValuesHolder(iv_heart,
                PropertyValuesHolder.ofFloat("scaleX",1.2f) ,
                PropertyValuesHolder.ofFloat("scaleY",1.2f)
                );

        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();

        animatText("Kayıp Dostlar");
        Glide.with(this).load("https://tenor.com/view/love-heart-heartbeat-gif-14809898")
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_kalpatis);

        Animation animation2 =AnimationUtils.loadAnimation(this ,R.anim.bottom_wave );

        iv_alt.setAnimation(animation2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                intent.putExtra("deger","");
                startActivity(intent);
//                startActivity(new Intent(SplashActivity.this
//                , MainActivity.class)
//                 .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        } ,4000);
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            tvheart.setText(charSequence.subSequence(0,index++));
            if(index<=charSequence.length()){
                    handler.postDelayed(runnable,delay);

            }
        }
    };
    public void animatText(CharSequence cs){
        charSequence =cs ;
        index=0;
        tvheart.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delay);
    }

}