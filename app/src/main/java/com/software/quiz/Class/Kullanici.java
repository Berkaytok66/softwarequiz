package com.software.quiz.Class;

public class Kullanici {
    String KullaniciIsmi, KullaniciEmail, KullaniciId,KullaniciPassword;

    public Kullanici(String kullaniciIsmi, String kullaniciEmail, String kullaniciId, String kullaniciPassword) {
        KullaniciIsmi = kullaniciIsmi;
        KullaniciEmail = kullaniciEmail;
        KullaniciId = kullaniciId;
        KullaniciPassword = kullaniciPassword;
    }

    public Kullanici() {
    }

    public String getKullaniciIsmi() {
        return KullaniciIsmi;
    }

    public void setKullaniciIsmi(String kullaniciIsmi) {
        KullaniciIsmi = kullaniciIsmi;
    }

    public String getKullaniciEmail() {
        return KullaniciEmail;
    }

    public void setKullaniciEmail(String kullaniciEmail) {
        KullaniciEmail = kullaniciEmail;
    }

    public String getKullaniciId() {
        return KullaniciId;
    }

    public void setKullaniciId(String kullaniciId) {
        KullaniciId = kullaniciId;
    }

    public String getKullaniciPassword() {
        return KullaniciPassword;
    }

    public void setKullaniciPassword(String kullaniciPassword) {
        KullaniciPassword = kullaniciPassword;
    }
}
