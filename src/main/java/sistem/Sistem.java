package sistem;

import database.Database;
import model.*;

/**
 * Class Sistem - Handler untuk proses bisnis sesuai sequence diagram
 */
public class Sistem {
    private Database database;
    
    public Sistem() {
        this.database = Database.getInstance();
    }
    
    /**
     * Menerima data pengunjung dari interface
     * Sesuai sequence diagram: kirim data pengunjung
     */
    public Pengunjung prosesDataPengunjung(String nama, int usia, JakCard jakCard) {
        Pengunjung pengunjung = new Pengunjung(nama, usia, jakCard);
        database.simpanPengunjung(pengunjung);
        return pengunjung;
    }
    
    /**
     * Cek saldo JakCard dari database
     * Sesuai sequence diagram: cek saldo jakCard -> saldo
     */
    public int cekSaldoJakCard(JakCard jakCard) {
        return jakCard.cekSaldo();
    }
    
    /**
     * Update saldo JakCard di database
     * Sesuai sequence diagram: Update saldo
     */
    public boolean updateSaldo(JakCard jakCard, int jumlahPengurangan) {
        return jakCard.kurangiSaldo(jumlahPengurangan);
    }
    
    /**
     * Simpan transaksi ke database
     * Sesuai sequence diagram: Simpan transaksi -> Status transaksi
     */
    public Transaksi simpanTransaksi(Pengunjung pengunjung, Tiket tiket, JakCard jakCard) {
        Transaksi transaksi = new Transaksi(pengunjung, tiket, jakCard);
        database.simpanTransaksi(transaksi);
        return transaksi;
    }
    
    /**
     * Proses transaksi lengkap
     * Sesuai flowchart: Bayar -> cek saldo -> proses pembayaran
     */
    public TransaksiResult prosesTransaksiLengkap(Pengunjung pengunjung, String jenisTiket, 
                                                   String kategori, int jumlahOrang) {
        JakCard jakCard = pengunjung.getJakCard();
        
        // Pilih tiket
        Tiket tiket = pengunjung.pilihTiket(jenisTiket, kategori, jumlahOrang);
        database.simpanTiket(tiket);
        
        // Buat transaksi
        Transaksi transaksi = new Transaksi(pengunjung, tiket, jakCard);
        
        // Validasi saldo
        if (!transaksi.validasiSaldo()) {
            transaksi.setStatus(Transaksi.STATUS_SALDO_TIDAK_CUKUP);
            database.simpanTransaksi(transaksi);
            return new TransaksiResult(false, transaksi, null, 
                "Saldo tidak cukup! Saldo: Rp " + String.format("%,d", jakCard.cekSaldo()) + 
                ", Total bayar: Rp " + String.format("%,d", transaksi.getTotalBayar()));
        }
        
        // Proses transaksi
        if (transaksi.prosesTransaksi()) {
            database.simpanTransaksi(transaksi);
            
            // Generate bukti pembelian
            BuktiPembelian bukti = new BuktiPembelian(transaksi);
            database.simpanBuktiPembelian(bukti);
            
            return new TransaksiResult(true, transaksi, bukti, "Transaksi berhasil!");
        }
        
        database.simpanTransaksi(transaksi);
        return new TransaksiResult(false, transaksi, null, "Transaksi gagal!");
    }
    
    /**
     * Top up saldo JakCard
     * Sesuai flowchart: Top up saldo
     */
    public boolean topUpSaldo(JakCard jakCard, int jumlah) {
        jakCard.topUpSaldo(jumlah);
        return true;
    }
    
    /**
     * Beli JakCard baru
     * Sesuai flowchart: Beli jak card
     */
    public JakCard beliJakCard() {
        JakCard jakCard = new JakCard();
        database.simpanJakCard(jakCard);
        return jakCard;
    }
    
    /**
     * Beli JakCard dengan saldo tertentu
     */
    public JakCard beliJakCard(int saldoAwal) {
        JakCard jakCard = new JakCard(saldoAwal);
        database.simpanJakCard(jakCard);
        return jakCard;
    }
    
    /**
     * Inner class untuk hasil transaksi
     */
    public static class TransaksiResult {
        private boolean berhasil;
        private Transaksi transaksi;
        private BuktiPembelian buktiPembelian;
        private String pesan;
        
        public TransaksiResult(boolean berhasil, Transaksi transaksi, 
                              BuktiPembelian buktiPembelian, String pesan) {
            this.berhasil = berhasil;
            this.transaksi = transaksi;
            this.buktiPembelian = buktiPembelian;
            this.pesan = pesan;
        }
        
        public boolean isBerhasil() {
            return berhasil;
        }
        
        public Transaksi getTransaksi() {
            return transaksi;
        }
        
        public BuktiPembelian getBuktiPembelian() {
            return buktiPembelian;
        }
        
        public String getPesan() {
            return pesan;
        }
    }
}
