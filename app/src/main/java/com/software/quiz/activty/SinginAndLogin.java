package com.software.quiz.activty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.software.quiz.Class.Kullanici;
import com.software.quiz.R;

import java.util.Timer;
import java.util.TimerTask;

public class SinginAndLogin extends AppCompatActivity {

    private ImageView hesapOlusturBtn,zatenHesabimVarBTN;
    private TextInputLayout KadiLayout,EmailLayout,SifreLayout;
    private TextInputEditText KadiEditText,EmailEditText,SifreEditText;
    private String Kadi,Gmail,Sifre;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private Kullanici mKullanici;
    private ProgressDialog progressDialog;
    private Timer myTimer;

    private void init(){//buttonları textleri layout dakiler ile bağlar
        hesapOlusturBtn = findViewById(R.id.hesapOlustur);
        zatenHesabimVarBTN = findViewById(R.id.hesabimVarBtn);
        KadiLayout = findViewById(R.id.textInputKullaniciAdiLayout);
        EmailLayout = findViewById(R.id.textInputGmailLayout);
        SifreLayout = findViewById(R.id.textInputSifreLayout);
        KadiEditText = findViewById(R.id.textInputKullaniciAdiEditText);
        EmailEditText = findViewById(R.id.textInputGmailEditText);
        SifreEditText = findViewById(R.id.textInputSifreEditText);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {//uygulama ilk calıştıgında başlar fonksiyonları buraya tanımlamak kod karmaşasına engel olur
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin_and_login);
        init();
        btnlar();
    }
    private void btnlar(){//login ekranındakı butonların işrevselligini saglar
        hesapOlusturBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SingIn();//kayıt olma funcsionu butona tıklanınca cagrılır
            }
        });
        zatenHesabimVarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(SinginAndLogin.this, LoginAndSingin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(x);
                overridePendingTransition(R.anim.slide_out_app,R.anim.slide_in_down);
            }
        });
    }

    public void SingIn(){
        Kadi = KadiEditText.getText().toString();
        Gmail = EmailEditText.getText().toString();
        Sifre = SifreEditText.getText().toString();

        if (!TextUtils.isEmpty(Kadi)){
            if (!TextUtils.isEmpty(Gmail)){
                if (!TextUtils.isEmpty(Sifre)){
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Lütfen Bekleyin...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();

                    //yenı kullanıcı bilgileri sunucuya eklenir
                    mAuth.createUserWithEmailAndPassword(Gmail,Sifre).addOnCompleteListener(SinginAndLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()){
                                     mUser = mAuth.getCurrentUser();//mUser inişilayz edilir
                                     if (mUser != null){//kullanıcı nın ici boş değil ise
                                         mKullanici = new Kullanici(Kadi,Gmail,mUser.getUid(),Sifre);//Kullanıcı Clasının içi doldurulur
                                         mFirestore.collection("Kullanicilar").document(mUser.getUid()).set(mKullanici)//doldurdugumuz clası daha sonra kullanıcı kayıtlarına bakabilmek icin ayrı bir veri tabanında cloud ederiz
                                                 .addOnCompleteListener(SinginAndLogin.this, new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){ // işlem başiarılı ise kullanıcıyı bilgilendırırız
                                                            progressDialog.setTitle("İşlem Başarılı");

                                                            myTimer = new Timer();
                                                            myTimer.schedule(new TimerTask() {
                                                                @Override
                                                                public void run() {
                                                                    progressDialog.dismiss();
                                                                }

                                                            }, 4000, 1000);
                                                        }else {
                                                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                            progressDialog.dismiss();
                                                        }
                                                     }
                                                 });
                                     }
                                 }else {
                                     Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                     progressDialog.dismiss();
                                 }
                        }
                    });



                }else
                    SifreLayout.setError("Şifre Boş Olamaz!!!");
            }else
                EmailLayout.setError("Gmail Boş Olamaz!!!");
        }else
            KadiLayout.setError("Kullanıcı Adı Boş Olamaz!!!");
    }
}