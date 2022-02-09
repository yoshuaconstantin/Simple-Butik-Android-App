package com.r0th.shopping.Model;

public class pembukuan_data_item {
    private String tanggal,totaltransaksi,totalunit,pakaian,alatshalat,hijab,aksesoris;


    public pembukuan_data_item(){

    }

    public pembukuan_data_item(String tanggal, String totaltransaksi, String totalunit, String pakaian, String alatshalat, String hijab, String aksesoris) {
        this.tanggal = tanggal;
        this.totaltransaksi = totaltransaksi;
        this.totalunit = totalunit;
        this.pakaian = pakaian;
        this.alatshalat = alatshalat;
        this.hijab = hijab;
        this.aksesoris = aksesoris;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotaltransaksi() {
        return totaltransaksi;
    }

    public void setTotaltransaksi(String totaltransaksi) {
        this.totaltransaksi = totaltransaksi;
    }

    public String getTotalunit() {
        return totalunit;
    }

    public void setTotalunit(String totalunit) {
        this.totalunit = totalunit;
    }

    public String getPakaian() {
        return pakaian;
    }

    public void setPakaian(String pakaian) {
        this.pakaian = pakaian;
    }

    public String getAlatshalat() {
        return alatshalat;
    }

    public void setAlatshalat(String alatshalat) {
        this.alatshalat = alatshalat;
    }

    public String getHijab() {
        return hijab;
    }

    public void setHijab(String hijab) {
        this.hijab = hijab;
    }

    public String getAksesoris() {
        return aksesoris;
    }

    public void setAksesoris(String aksesoris) {
        this.aksesoris = aksesoris;
    }
}
