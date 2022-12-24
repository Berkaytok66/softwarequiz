package com.software.quiz.Adapter;



import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.software.quiz.Class.DillerClas;
import com.software.quiz.Class.Quiz;
import com.software.quiz.OnLoadMoreListener;
import com.software.quiz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DillerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DillerClas> mClasList;
    private Context mContext;
    private View view;
    private DillerClas mClasDiller;
    private boolean isLoading = false;
    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 1;
    Dialog timeErorDialog;

    private Animation animation;
    private Dialog dialog;
    private Window window;
    private DatabaseReference databaseReference,uidRef ,rootRef ;
    private String derece,dilName;
    private int siraNumarasi = 1;
    private Query orderStatusQuery;
    private int dogruCount =0;
    private int yanlisCount =0;
    private int index = 0;
    private boolean btnKontrol = true;
    ArrayList<Quiz> qUizlist =new ArrayList<>();

    Quiz quizClas;
    private ArrayList<Quiz> quizArrayList;
    CardView cardViewA,cardViewB,cardViewC,cardViewD;


    public DillerAdapter(ArrayList<DillerClas> mClas, Context mContext,RecyclerView recyclerView) {

        this.mClasList = mClas;
        this.mContext = mContext; //bulundugumuz sayfada rahat işlem yapmak ıcın context tanımlarız


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()//Tıklandıgında aşagı dogru acılan pencerenın ayarlamaları
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)//Tıklandıgında aşagı dogru acılan pencerenın ayarlamaları
            {
                super.onScrolled(recyclerView, dx, dy);

                int total = getTotal(recyclerView);
                int lastVisible = getLastVisiblePosition(recyclerView);

                if (!getLoading() && total <= lastVisible + visibleThreshold)
                {
                    if (onLoadMoreListener != null)
                        onLoadMoreListener.onLoadMore();
                    setLoading(true);
                }
            }
        });

    }
    private int getTotal(RecyclerView recyclerView)//Tıklandıgında aşagı dogru acılan pencerenın ayarlamaları
    {
        if (recyclerView != null)
        {
            final RecyclerView.LayoutManager layoutManager = recyclerView
                    .getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager)
            {
                return layoutManager.getItemCount();
            }
        }
        return 0;
    }

    public int getLastVisiblePosition(RecyclerView recyclerView)//Tıklandıgında aşagı dogru acılan pencerenın ayarlamaları
    {
        if (recyclerView != null)
        {
            final RecyclerView.LayoutManager layoutManager = recyclerView
                    .getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager)
            {
                return ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
            }
        }
        return 0;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)//Tıklandıgında aşagı dogru acılan pencerenın ayarlamaları
    {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public boolean getLoading()
    {
        return isLoading;
    }//Tıklandıgında aşagı dogru acılan pencerenın ayarlamaları

    public void setLoading(boolean loading)
    {
        isLoading = loading;
    }//Tıklandıgında aşagı dogru acılan pencerenın ayarlamaları

    @Override//Tıklandıgında aşagı dogru acılan pencerenın ayarlamaları
    public int getItemViewType(int position)
    {
        if (mClasList.get(position) != null)
            return 1; //Item Data
        else
            return 2; //Item Loading
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //recycler view de kullanılacak model.layoutun inişilayz edilmesi

        view = LayoutInflater.from(mContext).inflate(R.layout.programing_diller,parent,false);
        return new DillerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DillerHolder){
            final DillerHolder viewHolder = (DillerHolder) holder;
            mClasDiller = mClasList.get(position);
            viewHolder.dilTextView.setText(mClasDiller.getDilTextView());//view icersindeki yazıları resimleri donen değerler ile doldurmak
            Picasso.get().load(mClasDiller.getImageID()).resize(70,70).into(viewHolder.dilImageView);// picasso kutuphanesi Resimleri dogru olculerine gore ayarlamanın bir yoludur


            boolean isExpanded = mClasList.get(position).isExpanded();
            viewHolder.constraintLayoutChild.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mClasList.size();
    }//Liste uzunlugunu alıp donurmek




    public class DillerHolder extends RecyclerView.ViewHolder{


        ImageView dilImageView;
        ImageButton startBtn;
        TextView dilTextView;
        ConstraintLayout constraintLayoutChild;
        ConstraintLayout constraintLayoutParent;
        private RadioGroup radioGroup;
        private RadioButton radioButton;

        LinearLayout linearLayout;
        ConstraintSet constraintSet;


        public DillerHolder(@NonNull View itemView) {
            super(itemView);
            dilImageView = itemView.findViewById(R.id.imageViewAvatar);
            dilTextView = itemView.findViewById(R.id.textViewTitle);
            constraintLayoutParent = itemView.findViewById(R.id.constraintLayoutParent);
            constraintLayoutChild = itemView.findViewById(R.id.constraintLayoutChild);
            startBtn = itemView.findViewById(R.id.startButton);
            radioGroup = itemView.findViewById(R.id.radioGroup);



            constraintLayoutParent.setOnClickListener(new View.OnClickListener()//tum setOnclıckListener yazanlar bır tıklanma yı ifade eder
            {
                @Override
                public void onClick(View v)
                {
                    DillerClas diller = mClasList.get(getAdapterPosition());
                    diller.setExpanded(!diller.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                    dilName = dilTextView.getText().toString();

                }
            });


            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectID = radioGroup.getCheckedRadioButtonId();
                    radioButton = itemView.findViewById(selectID);



                            switch (radioGroup.getCheckedRadioButtonId()){//zor kolay normak hangisini sectigimizi ayırt edebilmek ıcın kullanırız Swich case
                                case R.id.kolayRadioBTN:
                                    derece = String.valueOf(radioButton.getText());
                                    Kolay();
                                    break;

                                case R.id.ortaRadioBTN:

                                    Kolay();
                                    derece = String.valueOf(radioButton.getText());
                                    break;

                                case R.id.zorRadioBTN:
                                    Kolay();
                                    derece = String.valueOf(radioButton.getText());

                                    break;

                                default:
                                    Toast.makeText(itemView.getContext(),"Derece Secin",Toast.LENGTH_LONG).show();
                                    break;
                            }

                }
            });
        }

    }
    private void Kolay(){ //sectiğimize gore karşımıza dialog cıkar dialogu activite gibi kullanabiliriz
        dialog = new Dialog(mContext,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.page1);
        dialog.show();


        final int[][] deger = {{100}};
        final CountDownTimer[] countDownTimer = new CountDownTimer[1];
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        TextView cvpA,cvpB,cvpC,cvpD,soru;


        LinearLayout next_btn = dialog.findViewById(R.id.next_btn);
        ImageButton exitBtn = dialog.findViewById(R.id.exitBtn);

        cvpA = dialog.findViewById(R.id.cevap_a_textView);
        cvpB = dialog.findViewById(R.id.cevap_b_textView);
        cvpC = dialog.findViewById(R.id.cevap_c_textView);
        cvpD = dialog.findViewById(R.id.cevap_d_textView);
        soru = dialog.findViewById(R.id.soru_textView);
        cardViewA = dialog.findViewById(R.id.cevap_a_cardView);
        cardViewB = dialog.findViewById(R.id.cevap_b_cardView);
        cardViewC = dialog.findViewById(R.id.cevap_c_cardView);
        cardViewD = dialog.findViewById(R.id.cevap_d_cardView);

        countDownTimer[0] = new CountDownTimer(25000,250) {
            @Override
            public void onTick(long millisUntilFinished) {
                deger[0][0]--;
                progressBar.setProgress(deger[0][0]);


            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                timeErorDialog= new Dialog(dialog.getContext());
                timeErorDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                timeErorDialog.setContentView(R.layout.time_oopss_out);
                TextView dogruTextview = timeErorDialog.findViewById(R.id.dogrutextView);
                TextView yanlisTextview = timeErorDialog.findViewById(R.id.yanlisTextView);
                timeErorDialog.show();
                dogruTextview.setText("Doğru : "+dogruCount);
                yanlisTextview.setText("Yanlış : "+yanlisCount);
                LinearLayout linearLayoutTimeOpssBTN = timeErorDialog.findViewById(R.id.linea_time_opss_btn);
                linearLayoutTimeOpssBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeErorDialog.dismiss();
                        siraNumarasi =1;
                        dogruCount = 0;
                        yanlisCount = 0;
                        dialog.dismiss();
                    }
                });

            }
        }.start();
        databaseReference = FirebaseDatabase.getInstance().getReference(dilName); //Firebase icersindeki veri tabanına erişmemizi saglar


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    quizClas =dataSnapshot.getValue(Quiz.class);//quiz clasın içersini forlub ile doldururuz dolan her değer i de donguye gore listeye atarız
                    qUizlist.add(quizClas);


                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    qUizlist.forEach(quiz -> {
                        if (quiz.getSira() == siraNumarasi ){
                            if (quiz.getDerece().equals(derece)){

                                cvpA.setText(quiz.getA());//donen  degerleri soru ekranına yazıdırırız
                                cvpB.setText(quiz.getB());
                                cvpC.setText(quiz.getC());
                                cvpD.setText(quiz.getD());
                                soru.setText(quiz.getSoru());
                            }
                        }


                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show();
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siraNumarasi++;

                btnKontrol = true;
                resetColor();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    qUizlist.forEach(quiz -> {
                        if (quiz.getSira() == siraNumarasi ){
                            if (quiz.getDerece().equals(derece)){

                                cvpA.setText(quiz.getA());//donen  degerleri soru ekranına yazıdırırız
                                cvpB.setText(quiz.getB());
                                cvpC.setText(quiz.getC());
                                cvpD.setText(quiz.getD());
                                soru.setText(quiz.getSoru());
                            }
                        }




                    });
                }
                // Dogru();
            }
        });



        cvpA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnKontrol){
                    btnKontrol = false;
                    buttonlar(cvpA.getText().toString(),cardViewA);

                }else {
                    btnKontrol = false;

                }
            }
        });
        cvpB.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String quizStrinReply = cvpB.getText().toString();
               if (btnKontrol){
                   btnKontrol = false;
                   buttonlar(cvpB.getText().toString(),cardViewB);

               }else {
                   btnKontrol = false;

               }
           }
       });
        cvpC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnKontrol){
                    btnKontrol = false;
                    buttonlar(cvpC.getText().toString(),cardViewC);

                }else {
                    btnKontrol = false;

                }
            }
        });
        cvpD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnKontrol){
                    btnKontrol = false;
                    buttonlar(cvpD.getText().toString(),cardViewD);

                }else {
                    btnKontrol = false;

                }
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });






    }




    public void resetColor(){
        cardViewA.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        cardViewB.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        cardViewC.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        cardViewD.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }
    public void buttonlar(String quizStrinReply,CardView card){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            qUizlist.forEach(quiz -> {
                if (quiz.getSira() == siraNumarasi ){
                    if (quiz.getDerece().equals(derece)){
                        Toast.makeText(mContext,quiz.getCVP(),Toast.LENGTH_LONG).show();
                        if (Objects.equals(quiz.getCVP(), quizStrinReply)){
                            card.setBackgroundColor(mContext.getResources().getColor(R.color.Green));
                            dogruCount++;

                        }else{
                            card.setBackgroundColor(mContext.getResources().getColor(R.color.Orange));
                            yanlisCount++;
                        }
                    }


                }

            });
        }


    }

}

