package database;

import model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Database - Simulasi database untuk menyimpan data
 */
public class Database {
    private static Database instance;
    
    private List<Pengunjung> daftarPengunjung;
    private List<JakCard> daftarJakCard;
    private List<Tiket> daftarTiket;
    private List<Transaksi> daftarTransaksi;
    private List<BuktiPembelian> daftarBuktiPembelian;
    
    private Database() {
        daftarPengunjung = new ArrayList<>();
        daftarJakCard = new ArrayList<>();
        daftarTiket = new ArrayList<>();
        daftarTransaksi = new ArrayList<>();
        daftarBuktiPembelian = new ArrayList<>();
    }
    
    // Singleton pattern
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    
    // Pengunjung methods
    public void simpanPengunjung(Pengunjung pengunjung) {
        daftarPengunjung.add(pengunjung);
    }
    
    public List<Pengunjung> getDaftarPengunjung() {
        return daftarPengunjung;
    }
    
    // JakCard methods
    public void simpanJakCard(JakCard jakCard) {
        daftarJakCard.add(jakCard);
    }
    
    public List<JakCard> getDaftarJakCard() {
        return daftarJakCard;
    }
    
    public JakCard cekSaldoJakCard(int jakCardId) {
        for (JakCard jakCard : daftarJakCard) {
            if (jakCard.getId() == jakCardId) {
                return jakCard;
            }
        }
        return null;
    }
    
    public boolean updateSaldoJakCard(int jakCardId, int saldoBaru) {
        for (JakCard jakCard : daftarJakCard) {
            if (jakCard.getId() == jakCardId) {
                jakCard.setSaldo(saldoBaru);
                return true;
            }
        }
        return false;
    }
    
    // Tiket methods
    public void simpanTiket(Tiket tiket) {
        daftarTiket.add(tiket);
    }
    
    public List<Tiket> getDaftarTiket() {
        return daftarTiket;
    }
    
    // Transaksi methods
    public void simpanTransaksi(Transaksi transaksi) {
        daftarTransaksi.add(transaksi);
    }
    
    public List<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }
    
    public String getStatusTransaksi(int transaksiId) {
        for (Transaksi transaksi : daftarTransaksi) {
            if (transaksi.getId() == transaksiId) {
                return transaksi.getStatus();
            }
        }
        return "NOT FOUND";
    }
    
    // BuktiPembelian methods
    public void simpanBuktiPembelian(BuktiPembelian buktiPembelian) {
        daftarBuktiPembelian.add(buktiPembelian);
    }
    
    public List<BuktiPembelian> getDaftarBuktiPembelian() {
        return daftarBuktiPembelian;
    }
}
