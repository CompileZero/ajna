package in.ajna.ajnamobile.ajna;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import es.dmoral.toasty.Toasty;
import in.ajna.ajnamobile.ajna.Login.AgreeContinueActivity;
import in.ajna.ajnamobile.ajna.Login.LoginActivity;
import in.ajna.ajnamobile.ajna.Login.RegisterActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db;

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getSharedPreferences("DEVICE_CODE",MODE_PRIVATE);
        final String flagSignedIn = sp.getString("isSignedIn","0");

        if(flagSignedIn.equals("1")){
            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(flagSignedIn.equals("0")){
            Intent intent = new Intent(SplashScreenActivity.this,AgreeContinueActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        /*
        //Check whether the user has signed in
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            db = FirebaseFirestore.getInstance();
            Toasty.success(this,"Sign in successful!",Toast.LENGTH_SHORT,true).show();
            DocumentReference docRef=db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document=task.getResult();
                        if(document!=null){
                            if(document.get("fullName")==null || document.get("address")==null || document.get("city")==null){
                                Toast.makeText(SplashScreenActivity.this, "you have not registered yet!(Inner)", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                        else{

                            Toast.makeText(SplashScreenActivity.this, "you have not registered yet!(Outer)", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        else
        {
            //TODO:insert a toast
            Intent intent = new Intent(this,AgreeContinueActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }*/

    }
}
