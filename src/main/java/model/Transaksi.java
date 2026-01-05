package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class Transaksi - Merepresentasikan transaksi pembelian tiket
 */
public class Transaksi {
    private int id;
    private LocalDateTime tanggal;
    private String status;
    private int totalBayar;
    
    private Pengunjung pengunjung;
    private Tiket tiket;
    private JakCard jakCard;
    
    private static int counter = 1;
    
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_BERHASIL = "BERHASIL";
    public static final String STATUS_GAGAL = "GAGAL";
    public static final String STATUS_SALDO_TIDAK_CUKUP = "SALDO TIDAK CUKUP";
    
    public Transaksi(Pengunjung pengunjung, Tiket tiket, JakCard jakCard) {
        this.id = counter++;
        this.tanggal = LocalDateTime.now();
        this.status = STATUS_PENDING;
        this.pengunjung = pengunjung;
        this.tiket = tiket;
        this.jakCard = jakCard;
        this.totalBayar = tiket.getTotalHarga();
    }
    
    // Method validasiSaldo sesuai class diagram
    public boolean validasiSaldo() {
        return jakCard.cekSaldo() >= totalBayar;
    }
    
    // Method prosesTransaksi sesuai class diagram
    public boolean prosesTransaksi() {
        if (validasiSaldo()) {
            if (jakCard.kurangiSaldo(totalBayar)) {
                this.status = STATUS_BERHASIL;
                return true;
            }
        }
        this.status = STATUS_SALDO_TIDAK_CUKUP;
        return false;
    }
    
    // Getters dan Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDateTime getTanggal() {
        return tanggal;
    }
    
    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getTotalBayar() {
        return totalBayar;
    }
    
    public void setTotalBayar(int totalBayar) {
        this.totalBayar = totalBayar;
    }
    
    public Pengunjung getPengunjung() {
        return pengunjung;
    }
    
    public void setPengunjung(Pengunjung pengunjung) {
        this.pengunjung = pengunjung;
    }
    
    public Tiket getTiket() {
        return tiket;
    }
    
    public void setTiket(Tiket tiket) {
        this.tiket = tiket;
    }
    
    public JakCard getJakCard() {
        return jakCard;
    }
    
    public void setJakCard(JakCard jakCard) {
        this.jakCard = jakCard;
    }
    
    public String getTanggalFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        return tanggal.format(formatter);
    }
    
    @Override
    public String toString() {
        return "Transaksi{" +
                "id=" + id +
                ", tanggal=" + getTanggalFormatted() +
                ", status='" + status + '\'' +
                ", totalBayar=Rp " + String.format("%,d", totalBayar) +
                '}';
    }
}
