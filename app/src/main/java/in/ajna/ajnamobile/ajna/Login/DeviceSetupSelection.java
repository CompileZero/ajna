package in.ajna.ajnamobile.ajna.Login;

import androidx.appcompat.app.AppCompatActivity;
import in.ajna.ajnamobile.ajna.R;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hendraanggrian.widget.RevealFrameLayout;
import com.suke.widget.SwitchButton;

public class DeviceSetupSelection extends AppCompatActivity {


    View revealView,revealView2;
    SwitchButton btnSwitchWall,btnSwitchDoor;
    TextView tvWall,tv4,tvDoor,tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup_selection);

        btnSwitchWall=findViewById(R.id.btnSwitchWall);
        btnSwitchDoor=findViewById(R.id.btnSwitchDoor);

        tvWall=findViewById(R.id.tvWall);
        tv4=findViewById(R.id.tv4);
        tvDoor=findViewById(R.id.tvDoor);
        tv5=findViewById(R.id.tv5);

        revealView2=findViewById(R.id.revealView2);
        revealView=findViewById(R.id.revealView);


        btnSwitchWall.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked==true){
                    if(btnSwitchDoor.isChecked()) btnSwitchDoor.setChecked(false);
                    revealWall();
                    tvWall.setTextColor(getResources().getColor(R.color.white));
                    tv4.setTextColor(getResources().getColor(R.color.white));

                }
                else{
                    tvWall.setTextColor(getResources().getColor(R.color.c6));
                    tv4.setTextColor(getResources().getColor(R.color.black));
                    hideWall();
                }
            }
        });

        btnSwitchDoor.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked==true){
                    if(btnSwitchWall.isChecked()) btnSwitchWall.setChecked(false);
                    revealDoor();
                    tvDoor.setTextColor(getResources().getColor(R.color.white));
                    tv5.setTextColor(getResources().getColor(R.color.white));

                }
                else{
                    tvDoor.setTextColor(getResources().getColor(R.color.c6));
                    tv5.setTextColor(getResources().getColor(R.color.black));
                    hideDoor();
                }

            }
        });
    }

    public void revealWall(){
        int cx=revealView.getWidth();
        int cy=revealView.getHeight();

        float finalRadius=Math.max(cx,cy) * 1.2f;
        Animator reveal=ViewAnimationUtils
                .createCircularReveal(revealView,(int)(btnSwitchWall.getX()+150),(int)(btnSwitchWall.getY()+60),1f,finalRadius)
                .setDuration(600);
        reveal.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                revealView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        reveal.start();
    }
    public void hideWall(){
        int cx=revealView.getWidth();
        int cy=revealView.getHeight();

        float finalRadius=Math.max(cx,cy) * 1.2f;

        Animator hide=ViewAnimationUtils
                .createCircularReveal(revealView,(int)(btnSwitchWall.getX()+150),(int)(btnSwitchWall.getY()+60),finalRadius,1f)
                .setDuration(600);
        hide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                revealView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        hide.start();

    }

    public void revealDoor(){
        int cx=revealView2.getWidth();
        int cy=revealView2.getHeight();

        float finalRadius=Math.max(cx,cy) * 1.2f;
        Animator reveal=ViewAnimationUtils
                .createCircularReveal(revealView2,(int)(btnSwitchDoor.getX()+150),(int)(btnSwitchDoor.getY()+60),1f,finalRadius)
                .setDuration(600);

        reveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                revealView2.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        reveal.start();
    }

    public void hideDoor(){
        int cx=revealView2.getWidth();
        int cy=revealView2.getHeight();

        float finalRadius=Math.max(cx,cy) * 1.2f;

        Animator hide=ViewAnimationUtils
                .createCircularReveal(revealView2,(int)(btnSwitchDoor.getX()+150),(int)(btnSwitchDoor.getY()+60),finalRadius,1f)
                .setDuration(600);
        hide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                revealView2.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        hide.start();
    }
}