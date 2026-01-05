# 🏛️ MONAS Ticket System

> Sistem Pembelian Tiket Monumen Nasional Jakarta

Aplikasi desktop berbasis JavaFX untuk simulasi pembelian tiket masuk Monumen Nasional (MONAS) Jakarta. Dibuat sebagai tugas Pemrograman Berorientasi Objek.

---

## 📋 Daftar Isi
- [Fitur](#-fitur)
- [Teknologi](#-teknologi)
- [Persyaratan](#-persyaratan)
- [Cara Menjalankan](#-cara-menjalankan)
- [Struktur Project](#-struktur-project)
- [Screenshot](#-screenshot)
- [Class Diagram](#-class-diagram)

---

## ✨ Fitur

| Fitur | Deskripsi |
|-------|-----------|
| 🎫 Pembelian Tiket | Tiket Puncak & Museum |
| 💳 JakCard | Beli kartu baru atau gunakan yang sudah ada |
| 👥 Kategori Pengunjung | Dewasa, Mahasiswa, Anak-anak |
| 💰 Top-up Saldo | Isi ulang saldo JakCard |
| 🧾 Bukti Pembelian | Cetak struk transaksi |
| 🎨 Modern UI | Desain antarmuka modern dengan animasi |

---

## 🛠️ Teknologi

- **Java 17** - Bahasa pemrograman
- **JavaFX 17** - Framework GUI
- **Maven** - Build tool & dependency management
- **CSS** - Styling

---

## 📦 Persyaratan

- Java Development Kit (JDK) 17 atau lebih baru
- Apache Maven 3.6+

---

## 🚀 Cara Menjalankan

### Menggunakan Maven (Recommended)

```bash
# Clone atau extract project
cd "Remed oop"

# Compile dan jalankan
mvn clean javafx:run
```

### Menggunakan IDE

#### IntelliJ IDEA
1. File → Open → Pilih folder project
2. Tunggu Maven mengunduh dependencies
3. Klik kanan `MainApp.java` → Run

#### VS Code
1. Install **Extension Pack for Java**
2. Buka folder project
3. Klik Run pada `MainApp.java`

#### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Pilih folder project
3. Run As → Java Application → `MainApp`

---

## 📁 Struktur Project

```
Remed oop/
├── pom.xml                          # Maven configuration
├── README.md                        # Dokumentasi
└── src/
    └── main/
        ├── java/
        │   ├── application/
        │   │   └── MainApp.java          # Entry point aplikasi
        │   ├── controller/
        │   │   └── MonasController.java  # Controller UI & logic
        │   ├── database/
        │   │   └── Database.java         # Simulasi database (Singleton)
        │   ├── model/
        │   │   ├── BuktiPembelian.java   # Model bukti transaksi
        │   │   ├── JakCard.java          # Model kartu pembayaran
        │   │   ├── Pengunjung.java       # Model pengunjung
        │   │   ├── Tiket.java            # Model tiket
        │   │   └── Transaksi.java        # Model transaksi
        │   └── sistem/
        │       └── Sistem.java           # Business logic handler
        └── resources/
            └── styles.css                # Stylesheet UI
```

---

## 📸 Screenshot

### Halaman Utama
```
┌─────────────────────────────────────┐
│      🏛️ MONAS TICKET SYSTEM        │
│  Sistem Pembelian Tiket MONAS       │
├─────────────────────────────────────┤
│                                     │
│         [Ilustrasi MONAS]           │
│                                     │
│   Apakah Anda memiliki JakCard?     │
│                                     │
│  [Ya, Saya Punya] [Beli JakCard]    │
│                                     │
└─────────────────────────────────────┘
```

### Pilih Tiket
```
┌─────────────────────────────────────┐
│         Pilih Jenis Tiket           │
├─────────────────────────────────────┤
│  ┌─────────┐      ┌─────────┐       │
│  │ PUNCAK  │      │ MUSEUM  │       │
│  │ Rp24.000│      │ Rp8.000 │       │
│  └─────────┘      └─────────┘       │
└─────────────────────────────────────┘
```

---

## 📊 Class Diagram

```
┌─────────────┐     ┌─────────────┐
│  Pengunjung │────▶│   JakCard   │
├─────────────┤     ├─────────────┤
│ -id         │     │ -id         │
│ -nama       │     │ -saldo      │
│ -usia       │     ├─────────────┤
├─────────────┤     │ +cekSaldo() │
│ +pilihTiket()│    │ +kurangiSaldo()│
└─────────────┘     └─────────────┘
       │
       ▼
┌─────────────┐     ┌─────────────┐
│    Tiket    │◀────│  Transaksi  │
├─────────────┤     ├─────────────┤
│ -jenisTiket │     │ -tanggal    │
│ -kategori   │     │ -status     │
│ -harga      │     │ -totalBayar │
├─────────────┤     ├─────────────┤
│ +getHarga() │     │ +prosesTransaksi()│
└─────────────┘     └─────────────┘
                           │
                           ▼
                    ┌─────────────┐
                    │BuktiPembelian│
                    ├─────────────┤
                    │ -kodeTiket  │
                    │ -tanggalCetak│
                    ├─────────────┤
                    │ +cetakBukti()│
                    └─────────────┘
```

---

## 💰 Harga Tiket

| Jenis | Dewasa | Mahasiswa | Anak-anak |
|-------|--------|-----------|-----------|
| **Puncak** | Rp 24.000 | Rp 13.000 | Rp 6.000 |
| **Museum** | Rp 8.000 | Rp 5.000 | Rp 3.000 |

**Harga JakCard:** Rp 30.000 (termasuk saldo awal Rp 20.000)

---

## 👨‍💻 Author

Dibuat untuk memenuhi tugas Remedial Pemrograman Berorientasi Objek

---

## 📄 License

Project ini dibuat untuk keperluan akademik.

