package model;

/**
 * Class JakCard - Kartu pembayaran untuk tiket MONAS
 */
public class JakCard {
    private int id;
    private int saldo;
    
    private static int counter = 1;
    public static final int HARGA_KARTU = 30000;
    public static final int SALDO_AWAL = 20000;
    
    public JakCard() {
        this.id = counter++;
        this.saldo = SALDO_AWAL;
    }
    
    public JakCard(int saldoAwal) {
        this.id = counter++;
        this.saldo = saldoAwal;
    }
    
    // Method cekSaldo sesuai class diagram
    public int cekSaldo() {
        return this.saldo;
    }
    
    // Method kurangiSaldo sesuai class diagram
    public boolean kurangiSaldo(int jumlah) {
        if (jumlah <= this.saldo) {
            this.saldo -= jumlah;
            return true;
        }
        return false;
    }
    
    // Method untuk top up saldo
    public void topUpSaldo(int jumlah) {
        this.saldo += jumlah;
    }
    
    // Getters dan Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getSaldo() {
        return saldo;
    }
    
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
    
    @Override
    public String toString() {
        return "JakCard{" +
                "id=" + id +
                ", saldo=Rp " + String.format("%,d", saldo) +
                '}';
    }
}
