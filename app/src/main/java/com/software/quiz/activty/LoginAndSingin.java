package com.software.quiz.activty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.software.quiz.R;

import java.util.Timer;

public class LoginAndSingin extends AppCompatActivity {
    private Button LoginBtn;
    private ImageView hesapOlusturBtn;
    private TextInputLayout epostaLayout,sifreLayout;
    private TextInputEditText EpostaEditText,SifreEditText;
    private String Gmail,Sifre;
    private LinearLayout loginLinearLayout;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Timer myTimer;



    private void init(){//buttonları textleri layout dakiler ile bağlar
        LoginBtn = findViewById(R.id.LoginBtn);
        hesapOlusturBtn = findViewById(R.id.hesapOlusturBTN);
        epostaLayout = findViewById(R.id.eposaLinearInputLayout);
        sifreLayout = findViewById(R.id.sifreInputLayout);
        EpostaEditText =findViewById(R.id.PostaEditText);
        SifreEditText = findViewById(R.id.sifreEditText);

        mAuth= FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {//uygulama ilk calıştıgında başlar fonksiyonları buraya tanımlamak kod karmaşasına engel olur
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_singin);
        init();
        btnlar();
     //   zatenGrsYapildiysa();
    }

    private void btnlar(){//login ekranındakı butonların işrevselligini saglar
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap();
            }
        });
        hesapOlusturBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goNewHesap = new Intent(getApplicationContext(), SinginAndLogin.class);//intent methotu ile actıvıteler arası geçiş yapılır
                startActivity(goNewHesap);
                overridePendingTransition(R.anim.slide_out_app,R.anim.slide_in_down);// geciş sırasında kucuk bır anımasyon anım dosyasındakı slide_out_app ve  slide_in_down dosyaları sırayla calıştırılır
            }
        });
    }

    public void girisYap(){
        Gmail = EpostaEditText.getText().toString(); //edit text i tutacak bir Strıng tanımlanır
        Sifre = SifreEditText.getText().toString();
        if (!TextUtils.isEmpty(Gmail)){ // ,ici boşsa
            if (!TextUtils.isEmpty(Sifre)){ // şifre yazılmıssa
                progressDialog = new ProgressDialog(this);//kulanıcı ya bilgi icin dialog acılır
                progressDialog.setTitle("Lütfen Bekleyin");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setIndeterminate(true);
                progressDialog.show();//dialog burda actıf edilir
                mAuth.signInWithEmailAndPassword(Gmail,Sifre).addOnCompleteListener(LoginAndSingin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            progressDialog.dismiss();//işlem başarılı ise dialog kapanır
                            Intent intent = new Intent(LoginAndSingin.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//ana activiteye geçiş
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_out_app,R.anim.slide_in_down);


                        }else {//giriş başarılı değilse kullanıcıya sunucudan gelen hata mesajı döner
                            Toast.makeText(LoginAndSingin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }else//şifre boş bırakılmışsaaa
                sifreLayout.setError("Şifre Boş Olamaz!!!");
        }else
            epostaLayout.setError("Gmail Boş Olamaz!!!");
    }
    public void zatenGrsYapildiysa(){//zaten daha once giriş yapıldıysa tekrar sormaz
        if (mAuth != null){
            Intent x = new Intent(LoginAndSingin.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(x);
            finish();
            overridePendingTransition(R.anim.slide_out_app,R.anim.slide_in_down);
        }
    }
}