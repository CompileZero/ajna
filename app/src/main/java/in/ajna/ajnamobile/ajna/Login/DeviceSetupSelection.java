package in.ajna.ajnamobile.ajna.Login;

import androidx.appcompat.app.AppCompatActivity;
import in.ajna.ajnamobile.ajna.R;
import pl.droidsonroids.gif.GifImageView;

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


    private SwitchButton btnSwitchWall,btnSwitchDoor;
    private GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup_selection);

        btnSwitchWall=findViewById(R.id.btnSwitchWall);
        btnSwitchDoor=findViewById(R.id.btnSwitchDoor);
        gifImageView=findViewById(R.id.gifImageView);

        btnSwitchWall.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked==true){
                    if(btnSwitchDoor.isChecked()) btnSwitchDoor.setChecked(false);
                    gifImageView.setImageResource(R.drawable.setup_wall);
                }
            }
        });

        btnSwitchDoor.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked==true){
                    if(btnSwitchWall.isChecked()) btnSwitchWall.setChecked(false);
                    gifImageView.setImageResource(R.drawable.setup_door); }
            }
        });
    }


}