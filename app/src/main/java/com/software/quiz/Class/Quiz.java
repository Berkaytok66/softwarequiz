package com.software.quiz.Class;

public class Quiz {
    String A,B,C,D,Soru,CVP,Derece;
    int Sira;

    public Quiz(String a, String b, String c, String d, String soru, String CVP, String derece, int sira) {
        A = a;
        B = b;
        C = c;
        D = d;
        Soru = soru;
        this.CVP = CVP;
        Derece = derece;
        Sira = sira;
    }

    public Quiz() {
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getSoru() {
        return Soru;
    }

    public void setSoru(String soru) {
        Soru = soru;
    }

    public String getCVP() {
        return CVP;
    }

    public void setCVP(String CVP) {
        this.CVP = CVP;
    }

    public String getDerece() {
        return Derece;
    }

    public void setDerece(String derece) {
        Derece = derece;
    }

    public int getSira() {
        return Sira;
    }

    public void setSira(int sira) {
        Sira = sira;
    }
}
