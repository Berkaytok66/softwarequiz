package com.software.quiz.Class;

import android.widget.ImageView;
import android.widget.TextView;

import com.software.quiz.R;

import java.util.ArrayList;

public class DillerClas {
    private int imageID;
    private String dilTextView;
    private boolean expanded;

    public DillerClas(int imageID, String dilTextView) {
        this.imageID = imageID;
        this.dilTextView = dilTextView;
        this.expanded = false;
    }

    public DillerClas() {
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getDilTextView() {
        return dilTextView;
    }

    public void setDilTextView(String dilTextView) {
        this.dilTextView = dilTextView;
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }

    public static ArrayList<DillerClas> getData() {
        ArrayList<DillerClas> productList = new ArrayList<DillerClas>();
        int productImages[] = {
                R.drawable.java,
                R.drawable.csharp,
                R.drawable.kotlin,
                R.drawable.javascript,
                R.drawable.python,
                R.drawable.html_css};
        String[] productNames = {
                "Java",
                "C Sharp",
                "Kotlin",
                "Java Script",
                "Python",
                "Html and Css"
        };

        for (int i = 0; i < productImages.length; i++) {
            DillerClas temp = new DillerClas();
            temp.setImageID(productImages[i]);
            temp.setDilTextView(productNames[i]);

            productList.add(temp);

        }


        return productList;


    }
}
