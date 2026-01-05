package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Class BuktiPembelian - Merepresentasikan bukti pembelian tiket
 */
public class BuktiPembelian {
    private int id;
    private String kodeTiket;
    private LocalDateTime tanggalCetak;
    
    private Transaksi transaksi;
    
    private static int counter = 1;
    
    public BuktiPembelian(Transaksi transaksi) {
        this.id = counter++;
        this.transaksi = transaksi;
        this.kodeTiket = generateKodeTiket();
        this.tanggalCetak = LocalDateTime.now();
    }
    
    private String generateKodeTiket() {
        String prefix = "MNS";
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(5);
        String unique = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return prefix + "-" + timestamp + "-" + unique;
    }
    
    // Method cetakBukti sesuai class diagram
    public String cetakBukti() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        
        sb.append("============================================\n");
        sb.append("      BUKTI PEMBELIAN TIKET MONAS           \n");
        sb.append("         Monumen Nasional Jakarta           \n");
        sb.append("============================================\n");
        sb.append(String.format(" Kode Tiket    : %-24s \n", kodeTiket));
        sb.append(String.format(" Tanggal Cetak : %-24s \n", tanggalCetak.format(formatter)));
        sb.append("============================================\n");
        sb.append("              DATA PENGUNJUNG               \n");
        sb.append("============================================\n");
        sb.append(String.format(" Nama          : %-24s \n", transaksi.getPengunjung().getNama()));
        sb.append(String.format(" Usia          : %-24s \n", transaksi.getPengunjung().getUsia() + " tahun"));
        sb.append("============================================\n");
        sb.append("               DATA TIKET                   \n");
        sb.append("============================================\n");
        sb.append(String.format(" Jenis Tiket   : %-24s \n", transaksi.getTiket().getJenisTiket()));
        sb.append(String.format(" Kategori      : %-24s \n", transaksi.getTiket().getNamaTiket()));
        sb.append(String.format(" Harga/Tiket   : Rp %-21s \n", String.format("%,d", transaksi.getTiket().getHarga())));
        sb.append(String.format(" Jumlah Tiket  : %-24s \n", transaksi.getTiket().getJumlahTiket()));
        sb.append("============================================\n");
        sb.append(String.format(" TOTAL BAYAR   : Rp %-21s \n", String.format("%,d", transaksi.getTotalBayar())));
        sb.append(String.format(" STATUS        : %-24s \n", transaksi.getStatus()));
        sb.append("============================================\n");
        sb.append("         Terima kasih atas kunjungan        \n");
        sb.append("              Anda di MONAS!                \n");
        sb.append("============================================\n");
        
        return sb.toString();
    }
    
    // Getters dan Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getKodeTiket() {
        return kodeTiket;
    }
    
    public void setKodeTiket(String kodeTiket) {
        this.kodeTiket = kodeTiket;
    }
    
    public LocalDateTime getTanggalCetak() {
        return tanggalCetak;
    }
    
    public void setTanggalCetak(LocalDateTime tanggalCetak) {
        this.tanggalCetak = tanggalCetak;
    }
    
    public Transaksi getTransaksi() {
        return transaksi;
    }
    
    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }
    
    public String getTanggalCetakFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        return tanggalCetak.format(formatter);
    }
    
    @Override
    public String toString() {
        return "BuktiPembelian{" +
                "id=" + id +
                ", kodeTiket='" + kodeTiket + '\'' +
                ", tanggalCetak=" + getTanggalCetakFormatted() +
                '}';
    }
}
