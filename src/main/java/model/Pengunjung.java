package model;

/**
 * Class Pengunjung - Merepresentasikan pengunjung MONAS
 */
public class Pengunjung {
    private int id;
    private String nama;
    private int usia;
    private JakCard jakCard;
    private Tiket tiketDipilih;
    
    private static int counter = 1;
    
    public Pengunjung(String nama, int usia) {
        this.id = counter++;
        this.nama = nama;
        this.usia = usia;
    }
    
    public Pengunjung(String nama, int usia, JakCard jakCard) {
        this.id = counter++;
        this.nama = nama;
        this.usia = usia;
        this.jakCard = jakCard;
    }
    
    // Method pilihTiket sesuai class diagram
    public Tiket pilihTiket(String jenisTiket, String kategori, int jumlah) {
        int harga = hitungHarga(jenisTiket, kategori);
        this.tiketDipilih = new Tiket(jenisTiket, kategori, harga, jumlah);
        return this.tiketDipilih;
    }
    
    private int hitungHarga(String jenisTiket, String kategori) {
        if (jenisTiket.equalsIgnoreCase("Puncak")) {
            switch (kategori.toLowerCase()) {
                case "dewasa": return 24000;
                case "mahasiswa": return 13000;
                case "anak-anak": return 6000;
                default: return 0;
            }
        } else if (jenisTiket.equalsIgnoreCase("Museum")) {
            switch (kategori.toLowerCase()) {
                case "dewasa": return 8000;
                case "mahasiswa": return 5000;
                case "anak-anak": return 3000;
                default: return 0;
            }
        }
        return 0;
    }
    
    // Getters dan Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public int getUsia() {
        return usia;
    }
    
    public void setUsia(int usia) {
        this.usia = usia;
    }
    
    public JakCard getJakCard() {
        return jakCard;
    }
    
    public void setJakCard(JakCard jakCard) {
        this.jakCard = jakCard;
    }
    
    public Tiket getTiketDipilih() {
        return tiketDipilih;
    }
    
    public void setTiketDipilih(Tiket tiketDipilih) {
        this.tiketDipilih = tiketDipilih;
    }
    
    @Override
    public String toString() {
        return "Pengunjung{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", usia=" + usia +
                '}';
    }
}
