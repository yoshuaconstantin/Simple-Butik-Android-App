package com.r0th.shopping.Model;

public class pembukuan_data_item {
    private String tanggal,totaltransaksi,totalunit,kategori;


    public pembukuan_data_item(){

    }

    public pembukuan_data_item(String tanggal, String totaltransaksi, String totalunit, String kategori) {
        this.tanggal = tanggal;
        this.totaltransaksi = totaltransaksi;
        this.totalunit = totalunit;
        this.kategori = kategori;
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

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
