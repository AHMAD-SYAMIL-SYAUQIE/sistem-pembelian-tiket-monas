package model;

/**
 * Class Tiket - Merepresentasikan tiket MONAS
 */
public class Tiket {
    private int id;
    private String namaTiket;
    private String jenisTiket;
    private int harga;
    private int jumlahTiket;
    
    private static int counter = 1;
    
    public Tiket(String jenisTiket, String namaTiket, int harga, int jumlahTiket) {
        this.id = counter++;
        this.jenisTiket = jenisTiket;
        this.namaTiket = namaTiket;
        this.harga = harga;
        this.jumlahTiket = jumlahTiket;
    }
    
    // Method getHarga sesuai class diagram
    public int getHarga() {
        return this.harga;
    }
    
    // Method untuk mendapatkan total harga
    public int getTotalHarga() {
        return this.harga * this.jumlahTiket;
    }
    
    // Getters dan Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNamaTiket() {
        return namaTiket;
    }
    
    public void setNamaTiket(String namaTiket) {
        this.namaTiket = namaTiket;
    }
    
    public String getJenisTiket() {
        return jenisTiket;
    }
    
    public void setJenisTiket(String jenisTiket) {
        this.jenisTiket = jenisTiket;
    }
    
    public void setHarga(int harga) {
        this.harga = harga;
    }
    
    public int getJumlahTiket() {
        return jumlahTiket;
    }
    
    public void setJumlahTiket(int jumlahTiket) {
        this.jumlahTiket = jumlahTiket;
    }
    
    @Override
    public String toString() {
        return "Tiket{" +
                "id=" + id +
                ", jenisTiket='" + jenisTiket + '\'' +
                ", kategori='" + namaTiket + '\'' +
                ", harga=Rp " + String.format("%,d", harga) +
                ", jumlah=" + jumlahTiket +
                '}';
    }
}
