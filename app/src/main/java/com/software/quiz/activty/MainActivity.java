package com.software.quiz.activty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.software.quiz.Adapter.DillerAdapter;
import com.software.quiz.Class.DillerClas;
import com.software.quiz.Class.Quiz;
import com.software.quiz.OnLoadMoreListener;
import com.software.quiz.R;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore mFireStor;
    private DocumentReference mRef;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ArrayList<DillerClas> mList;
    private DillerAdapter myAdapter;
    private Button adminBtn,adminEkleBtn;
    private Quiz mQuiz;

    private Dialog dialog;
    private Window window;
    private TextInputEditText yazilimDiliEditText,dereceEditText,soruEditText,aEditText
            ,bEditText,cEditText,dEditText,cvpEditText;
    private EditText siraEditText;
    private TextInputLayout programDiliInputLayout,dereceInputLayout,siraInputLayout,soruInputLayout,AInputLayout,
            BInputLayout,CInputLayout,DInputLayout,CvpInputLayout;
    private String yazilimDili,derece,soru,a,b,c,sira,d,cvp;

    private DatabaseReference mDatabaseRef;


    private void init(){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStor = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rw);
        mList = new ArrayList<DillerClas>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        adminBtn = findViewById(R.id.admin_btn_id);




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        RecyclerViewSetting();

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminFunctions();
            }
        });

    }


    private void RecyclerViewSetting(){//Dillerimizin oldugu rcycler view ve adapterin burda ayarlamaları yapılır

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        myAdapter = new DillerAdapter(DillerClas.getData(),this,recyclerView);
        recyclerView.setAdapter(myAdapter);



    }
    private void adminFunctions() {
        dialog = new Dialog(MainActivity.this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.admin_page);
        dialog.show();
        adminEkleBtn = dialog.findViewById(R.id.admin_ekle_btn);

        programDiliInputLayout = dialog.findViewById(R.id.programDiliInputLayout);
        dereceInputLayout = dialog.findViewById(R.id.dereceInputLayout);
        siraInputLayout = dialog.findViewById(R.id.siraInputLayout);
        soruInputLayout = dialog.findViewById(R.id.soruInputLayout);
        AInputLayout = dialog.findViewById(R.id.AInputLayout);
        BInputLayout = dialog.findViewById(R.id.BInputLayout);
        CInputLayout = dialog.findViewById(R.id.CInputLayout);
        DInputLayout = dialog.findViewById(R.id.DInputLayout);
        CvpInputLayout = dialog.findViewById(R.id.CvpInputLayout);

        yazilimDiliEditText = dialog.findViewById(R.id.ProgramDiliEditText);
        dereceEditText = dialog.findViewById(R.id.dereceEditText);
        siraEditText = dialog.findViewById(R.id.siraEditText);
        soruEditText = dialog.findViewById(R.id.soruEditText);
        aEditText = dialog.findViewById(R.id.AEditText);
        bEditText = dialog.findViewById(R.id.BEditText);
        cEditText = dialog.findViewById(R.id.CEditText);
        dEditText = dialog.findViewById(R.id.DEditText);
        cvpEditText = dialog.findViewById(R.id.CvpEditText);



        adminEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //stringlerin icini doldurma
                yazilimDili = yazilimDiliEditText.getText().toString();
                derece = dereceEditText.getText().toString();
                sira = siraEditText.getText().toString();
                soru = soruEditText.getText().toString();



                a = aEditText.getText().toString();
                b = bEditText.getText().toString();
                c = cEditText.getText().toString();
                d = dEditText.getText().toString();
                cvp = cvpEditText.getText().toString();

                String RandomUuid = UUID.randomUUID().toString();

                if (!TextUtils.isEmpty(yazilimDili)){
                    if (!TextUtils.isEmpty(derece)){
                        if (!TextUtils.isEmpty(sira)){
                            int i = Integer.parseInt(sira);
                            if (!TextUtils.isEmpty(soru)){
                                if (!TextUtils.isEmpty(a)){
                                    if (!TextUtils.isEmpty(b)){
                                        if (!TextUtils.isEmpty(c)){
                                            if (!TextUtils.isEmpty(d)){
                                                if (!TextUtils.isEmpty(cvp)){

                                                        mQuiz = new Quiz(a,b,c,d,soru,cvp,derece,i);
                                                        mDatabaseRef.child(yazilimDili).child(RandomUuid).setValue(mQuiz);
                                                        x();

                                                }else {CvpInputLayout.setError("CVP boş olamaz !!!");}
                                            }else {DInputLayout.setError("D Sikki boş olamaz!!!");}
                                        }else {CInputLayout.setError("C Sikki boş olamaz!!!");}
                                    }else {BInputLayout.setError("B Sikki boş olamaz!!!");}
                                }else {AInputLayout.setError("A Sikki boş olamaz!!!");}
                            }else {soruInputLayout.setError("Soru Boş Bırakılamaz!!");}
                        }else {siraInputLayout.setError("Soru sirasi boş olamaz!!!");}
                    }else {dereceInputLayout.setError("Derece Boş Olamaz");}
                }else {programDiliInputLayout.setError("Dili Boş Bırakmayın");}

                //Button İci Bitiş
            }
        });



    }

    private void x(){
        yazilimDiliEditText.setText(null);
        dereceEditText.setText(null);
        siraEditText.setText(null);
        soruEditText.setText(null);
        aEditText.setText(null);
        bEditText.setText(null);
        cEditText.setText(null);
        dEditText.setText(null);
        cvpEditText.setText(null);
    }
}



