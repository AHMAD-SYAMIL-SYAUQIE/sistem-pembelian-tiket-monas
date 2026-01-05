package controller;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import sistem.Sistem;
import sistem.Sistem.TransaksiResult;

import java.time.format.DateTimeFormatter;

/**
 * Controller untuk aplikasi pembelian tiket MONAS
 * Mengimplementasikan flow sesuai sequence diagram dan flowchart
 */
public class MonasController {
    private Sistem sistem;
    private JakCard currentJakCard;
    private Pengunjung currentPengunjung;
    
    private BorderPane mainLayout;
    private VBox contentArea;
    private Label saldoLabel;
    private Stage primaryStage;
    
    // State management
    private String selectedJenisTiket;
    private String selectedKategori;
    private int jumlahOrang;
    
    public MonasController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.sistem = new Sistem();
    }
    
    public BorderPane createMainLayout() {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("main-layout");
        
        // Header
        VBox header = createHeader();
        mainLayout.setTop(header);
        
        // Content Area
        contentArea = new VBox(20);
        contentArea.setAlignment(Pos.TOP_CENTER);
        contentArea.setPadding(new Insets(30));
        contentArea.getStyleClass().add("content-area");
        
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        mainLayout.setCenter(scrollPane);
        
        // Start with JakCard check - sesuai flowchart "Punya jak card?"
        showJakCardCheck();
        
        return mainLayout;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.getStyleClass().add("header");
        
        // Logo and title
        HBox titleBox = new HBox(15);
        titleBox.setAlignment(Pos.CENTER);
        
        // Monas Icon - detailed representation
        StackPane monasIcon = createMonasIcon(60);
        
        Label titleLabel = new Label("MONAS TICKET SYSTEM");
        titleLabel.getStyleClass().add("header-title");
        
        titleBox.getChildren().addAll(monasIcon, titleLabel);
        
        Label subtitleLabel = new Label("Sistem Pembelian Tiket Monumen Nasional Jakarta");
        subtitleLabel.getStyleClass().add("header-subtitle");
        
        // Saldo display
        HBox saldoBox = new HBox(10);
        saldoBox.setAlignment(Pos.CENTER);
        saldoBox.getStyleClass().add("saldo-box");
        
        Label saldoTextLabel = new Label("Saldo JakCard:");
        saldoTextLabel.getStyleClass().add("saldo-text");
        
        saldoLabel = new Label("Rp 0");
        saldoLabel.getStyleClass().add("saldo-amount");
        
        saldoBox.getChildren().addAll(saldoTextLabel, saldoLabel);
        saldoBox.setVisible(false);
        
        header.getChildren().addAll(titleBox, subtitleLabel, saldoBox);
        
        return header;
    }
    
    /**
     * Step 1: Check JakCard - sesuai flowchart
     */
    private void showJakCardCheck() {
        contentArea.getChildren().clear();
        
        VBox card = createCard("Selamat Datang di MONAS", "");
        
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        
        // Add Monas illustration
        StackPane monasIllustration = createLargeMonasIllustration();
        
        Label questionLabel = new Label("Apakah Anda sudah memiliki JakCard?");
        questionLabel.getStyleClass().add("question-label");
        
        // Info box
        VBox infoBox = new VBox(10);
        infoBox.getStyleClass().add("info-box");
        infoBox.setAlignment(Pos.CENTER_LEFT);
        
        Label infoTitle = new Label("[i] Informasi JakCard");
        infoTitle.getStyleClass().add("info-title");
        
        Label info1 = new Label("* Harga JakCard: Rp 30.000,-");
        Label info2 = new Label("* Saldo Awal: Rp 20.000,-");
        Label info3 = new Label("* Total: Rp 50.000,-");
        info1.getStyleClass().add("info-text");
        info2.getStyleClass().add("info-text");
        info3.getStyleClass().add("info-text");
        
        infoBox.getChildren().addAll(infoTitle, info1, info2, info3);
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button yesButton = createPrimaryButton("Ya, Saya Punya");
        yesButton.setOnAction(e -> showInputSaldo());
        
        Button noButton = createSecondaryButton("Belum, Beli JakCard");
        noButton.setOnAction(e -> showBeliJakCard());
        
        buttonBox.getChildren().addAll(yesButton, noButton);
        
        content.getChildren().addAll(monasIllustration, questionLabel, infoBox, buttonBox);
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    /**
     * Input saldo untuk user yang sudah punya JakCard
     */
    private void showInputSaldo() {
        contentArea.getChildren().clear();
        
        VBox card = createCard("Masukkan Saldo JakCard", "Silakan masukkan saldo JakCard Anda saat ini");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        
        TextField saldoField = createTextField("Masukkan saldo (Rp)");
        saldoField.setMaxWidth(300);
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button backButton = createSecondaryButton("<< Kembali");
        backButton.setOnAction(e -> showJakCardCheck());
        
        Button nextButton = createPrimaryButton("Lanjutkan >>");
        nextButton.setOnAction(e -> {
            try {
                int saldo = Integer.parseInt(saldoField.getText().replace(".", "").replace(",", ""));
                currentJakCard = sistem.beliJakCard(saldo);
                updateSaldoDisplay();
                showInputPengunjung();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Masukkan angka yang valid!", Alert.AlertType.ERROR);
            }
        });
        
        buttonBox.getChildren().addAll(backButton, nextButton);
        content.getChildren().addAll(saldoField, buttonBox);
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    /**
     * Beli JakCard - sesuai flowchart "Beli jak card"
     */
    private void showBeliJakCard() {
        contentArea.getChildren().clear();
        
        VBox card = createCard("Pembelian JakCard", "JakCard diperlukan untuk membeli tiket MONAS");
        
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        
        // JakCard preview
        VBox jakCardPreview = new VBox(10);
        jakCardPreview.getStyleClass().add("jakcard-preview");
        jakCardPreview.setAlignment(Pos.CENTER);
        jakCardPreview.setMaxWidth(350);
        
        Label bankLabel = new Label("BANK DKI");
        bankLabel.getStyleClass().add("jakcard-bank");
        
        Label cardTitle = new Label("JakCard");
        cardTitle.getStyleClass().add("jakcard-title");
        
        Label cardDesc = new Label("Plaza Selatan Monas");
        cardDesc.getStyleClass().add("jakcard-desc");
        
        jakCardPreview.getChildren().addAll(bankLabel, cardTitle, cardDesc);
        
        // Price breakdown
        GridPane priceGrid = new GridPane();
        priceGrid.setHgap(20);
        priceGrid.setVgap(10);
        priceGrid.setAlignment(Pos.CENTER);
        
        priceGrid.add(new Label("Harga JakCard"), 0, 0);
        priceGrid.add(new Label(": Rp 30.000,-"), 1, 0);
        priceGrid.add(new Label("Saldo Awal"), 0, 1);
        priceGrid.add(new Label(": Rp 20.000,-"), 1, 1);
        
        Separator sep = new Separator();
        priceGrid.add(sep, 0, 2, 2, 1);
        
        Label totalLabel = new Label("Total");
        totalLabel.setStyle("-fx-font-weight: bold;");
        Label totalAmount = new Label(": Rp 50.000,-");
        totalAmount.setStyle("-fx-font-weight: bold; -fx-text-fill: #C41E3A;");
        priceGrid.add(totalLabel, 0, 3);
        priceGrid.add(totalAmount, 1, 3);
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button backButton = createSecondaryButton("<< Kembali");
        backButton.setOnAction(e -> showJakCardCheck());
        
        Button buyButton = createPrimaryButton("Beli JakCard");
        buyButton.setOnAction(e -> {
            currentJakCard = sistem.beliJakCard();
            updateSaldoDisplay();
            showSuccessJakCard();
        });
        
        buttonBox.getChildren().addAll(backButton, buyButton);
        content.getChildren().addAll(jakCardPreview, priceGrid, buttonBox);
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    private void showSuccessJakCard() {
        contentArea.getChildren().clear();
        
        VBox card = createCard("JakCard Berhasil Dibeli!", "");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        
        // Success icon
        Circle successCircle = new Circle(40);
        successCircle.setFill(Color.web("#28a745"));
        Label checkMark = new Label("V");
        checkMark.setStyle("-fx-font-size: 40px; -fx-text-fill: white; -fx-font-weight: bold;");
        StackPane successIcon = new StackPane(successCircle, checkMark);
        
        Label successMsg = new Label("JakCard Anda siap digunakan!");
        successMsg.getStyleClass().add("success-message");
        
        Label saldoInfo = new Label("Saldo tersedia: Rp " + String.format("%,d", currentJakCard.cekSaldo()));
        saldoInfo.getStyleClass().add("saldo-info");
        
        Button continueButton = createPrimaryButton("Lanjutkan ke Pembelian Tiket >>");
        continueButton.setOnAction(e -> showInputPengunjung());
        
        content.getChildren().addAll(successIcon, successMsg, saldoInfo, continueButton);
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    /**
     * Input data pengunjung - sesuai sequence diagram "Input data pengunjung"
     */
    private void showInputPengunjung() {
        contentArea.getChildren().clear();
        
        VBox card = createCard("Data Pengunjung", "Masukkan data pengunjung untuk pembelian tiket");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(400);
        
        TextField namaField = createTextField("Nama Lengkap");
        TextField usiaField = createTextField("Usia");
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button backButton = createSecondaryButton("<< Kembali");
        backButton.setOnAction(e -> showJakCardCheck());
        
        Button nextButton = createPrimaryButton("Lanjutkan >>");
        nextButton.setOnAction(e -> {
            String nama = namaField.getText().trim();
            String usiaText = usiaField.getText().trim();
            
            if (nama.isEmpty() || usiaText.isEmpty()) {
                showAlert("Error", "Semua field harus diisi!", Alert.AlertType.ERROR);
                return;
            }
            
            try {
                int usia = Integer.parseInt(usiaText);
                currentPengunjung = sistem.prosesDataPengunjung(nama, usia, currentJakCard);
                showPilihJenisTiket();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Usia harus berupa angka!", Alert.AlertType.ERROR);
            }
        });
        
        buttonBox.getChildren().addAll(backButton, nextButton);
        content.getChildren().addAll(namaField, usiaField, buttonBox);
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        
        contentArea.getChildren().add(card);
        animateIn(card);
        
        // Show saldo box
        ((VBox) mainLayout.getTop()).getChildren().get(2).setVisible(true);
    }
    
    /**
     * Pilih jenis tiket - sesuai flowchart "Pilih jenis tiket"
     */
    private void showPilihJenisTiket() {
        contentArea.getChildren().clear();
        
        VBox card = createCard("Pilih Jenis Tiket", "Silakan pilih jenis tiket yang ingin dibeli");
        
        HBox ticketOptions = new HBox(30);
        ticketOptions.setAlignment(Pos.CENTER);
        
        // Tiket Puncak
        VBox puncakCard = createTicketTypeCard(
            "PUNCAK",
            "Tiket Puncak",
            "Nikmati pemandangan Jakarta dari puncak Monas",
            new String[]{"Dewasa: Rp 24.000", "Mahasiswa: Rp 13.000", "Anak-anak: Rp 6.000"},
            () -> {
                selectedJenisTiket = "Puncak";
                showPilihKategori();
            }
        );
        
        // Tiket Museum
        VBox museumCard = createTicketTypeCard(
            "MUSEUM",
            "Tiket Museum",
            "Jelajahi sejarah Indonesia di Museum Monas",
            new String[]{"Dewasa: Rp 8.000", "Mahasiswa: Rp 5.000", "Anak-anak: Rp 3.000"},
            () -> {
                selectedJenisTiket = "Museum";
                showPilihKategori();
            }
        );
        
        ticketOptions.getChildren().addAll(puncakCard, museumCard);
        
        Button backButton = createSecondaryButton("<< Kembali");
        backButton.setOnAction(e -> showInputPengunjung());
        
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(ticketOptions, backButton);
        
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    private VBox createTicketTypeCard(String icon, String title, String desc, String[] prices, Runnable onSelect) {
        VBox card = new VBox(15);
        card.getStyleClass().add("ticket-type-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setMinWidth(280);
        card.setMaxWidth(280);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #C41E3A;");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("ticket-type-title");
        
        Label descLabel = new Label(desc);
        descLabel.getStyleClass().add("ticket-type-desc");
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(TextAlignment.CENTER);
        
        Separator sep = new Separator();
        
        VBox priceBox = new VBox(5);
        priceBox.setAlignment(Pos.CENTER_LEFT);
        for (String price : prices) {
            Label priceLabel = new Label("* " + price);
            priceLabel.getStyleClass().add("ticket-price-item");
            priceBox.getChildren().add(priceLabel);
        }
        
        Button selectButton = createPrimaryButton("Pilih >>");
        selectButton.setMaxWidth(Double.MAX_VALUE);
        selectButton.setOnAction(e -> onSelect.run());
        
        card.getChildren().addAll(iconLabel, titleLabel, descLabel, sep, priceBox, selectButton);
        
        // Hover effect
        card.setOnMouseEntered(e -> card.getStyleClass().add("ticket-type-card-hover"));
        card.setOnMouseExited(e -> card.getStyleClass().remove("ticket-type-card-hover"));
        
        // Click on card juga trigger select
        card.setOnMouseClicked(e -> onSelect.run());
        
        return card;
    }
    
    /**
     * Pilih kategori - sesuai flowchart "Pilih Kategori"
     */
    private void showPilihKategori() {
        contentArea.getChildren().clear();
        
        VBox card = createCard("Pilih Kategori - " + selectedJenisTiket, 
                              "Pilih kategori pengunjung untuk tiket " + selectedJenisTiket);
        
        HBox categoryOptions = new HBox(20);
        categoryOptions.setAlignment(Pos.CENTER);
        
        int hargaDewasa = selectedJenisTiket.equals("Puncak") ? 24000 : 8000;
        int hargaMahasiswa = selectedJenisTiket.equals("Puncak") ? 13000 : 5000;
        int hargaAnak = selectedJenisTiket.equals("Puncak") ? 6000 : 3000;
        
        VBox dewasaCard = createCategoryCard("DEWASA", "Dewasa", "Usia > 17 tahun", hargaDewasa, () -> {
            selectedKategori = "Dewasa";
            showInputJumlah();
        });
        
        VBox mahasiswaCard = createCategoryCard("MHS", "Mahasiswa", "Dengan KTM", hargaMahasiswa, () -> {
            selectedKategori = "Mahasiswa";
            showInputJumlah();
        });
        
        VBox anakCard = createCategoryCard("ANAK", "Anak-anak", "Usia <= 12 tahun", hargaAnak, () -> {
            selectedKategori = "Anak-anak";
            showInputJumlah();
        });
        
        categoryOptions.getChildren().addAll(dewasaCard, mahasiswaCard, anakCard);
        
        Button backButton = createSecondaryButton("<< Kembali");
        backButton.setOnAction(e -> showPilihJenisTiket());
        
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(categoryOptions, backButton);
        
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    private VBox createCategoryCard(String icon, String title, String desc, int harga, Runnable onSelect) {
        VBox card = new VBox(10);
        card.getStyleClass().add("category-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setMinWidth(180);
        card.setCursor(javafx.scene.Cursor.HAND);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #C41E3A;");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("category-title");
        
        Label descLabel = new Label(desc);
        descLabel.getStyleClass().add("category-desc");
        
        Label priceLabel = new Label("Rp " + String.format("%,d", harga));
        priceLabel.getStyleClass().add("category-price");
        
        Button selectBtn = createPrimaryButton("Pilih");
        selectBtn.setOnAction(e -> onSelect.run());
        
        card.getChildren().addAll(iconLabel, titleLabel, descLabel, priceLabel, selectBtn);
        
        card.setOnMouseEntered(e -> card.getStyleClass().add("category-card-hover"));
        card.setOnMouseExited(e -> card.getStyleClass().remove("category-card-hover"));
        card.setOnMouseClicked(e -> onSelect.run());
        
        return card;
    }
    
    /**
     * Input jumlah orang - sesuai flowchart "masukkan Jumlah orang"
     */
    private void showInputJumlah() {
        contentArea.getChildren().clear();
        
        int hargaSatuan = getHargaTiket(selectedJenisTiket, selectedKategori);
        
        VBox card = createCard("Jumlah Tiket", 
                              selectedJenisTiket + " - " + selectedKategori + " (Rp " + String.format("%,d", hargaSatuan) + "/tiket)");
        
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        
        // Quantity selector
        HBox quantityBox = new HBox(15);
        quantityBox.setAlignment(Pos.CENTER);
        
        final int[] quantity = {1};
        
        Button minusBtn = new Button("-");
        minusBtn.getStyleClass().add("quantity-btn");
        
        Label quantityLabel = new Label("1");
        quantityLabel.getStyleClass().add("quantity-label");
        quantityLabel.setMinWidth(60);
        quantityLabel.setAlignment(Pos.CENTER);
        
        Button plusBtn = new Button("+");
        plusBtn.getStyleClass().add("quantity-btn");
        
        // Total display
        Label totalLabel = new Label("Total: Rp " + String.format("%,d", hargaSatuan));
        totalLabel.getStyleClass().add("total-label");
        
        minusBtn.setOnAction(e -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                quantityLabel.setText(String.valueOf(quantity[0]));
                totalLabel.setText("Total: Rp " + String.format("%,d", hargaSatuan * quantity[0]));
            }
        });
        
        plusBtn.setOnAction(e -> {
            quantity[0]++;
            quantityLabel.setText(String.valueOf(quantity[0]));
            totalLabel.setText("Total: Rp " + String.format("%,d", hargaSatuan * quantity[0]));
        });
        
        quantityBox.getChildren().addAll(minusBtn, quantityLabel, plusBtn);
        
        // Saldo info
        VBox saldoInfo = new VBox(5);
        saldoInfo.setAlignment(Pos.CENTER);
        saldoInfo.getStyleClass().add("saldo-info-box");
        
        Label currentSaldo = new Label("Saldo JakCard: Rp " + String.format("%,d", currentJakCard.cekSaldo()));
        currentSaldo.getStyleClass().add("current-saldo");
        
        saldoInfo.getChildren().add(currentSaldo);
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button backButton = createSecondaryButton("<< Kembali");
        backButton.setOnAction(e -> showPilihKategori());
        
        Button payButton = createPrimaryButton("Bayar >>");
        payButton.setOnAction(e -> {
            jumlahOrang = quantity[0];
            prosessPembayaran();
        });
        
        buttonBox.getChildren().addAll(backButton, payButton);
        content.getChildren().addAll(quantityBox, totalLabel, saldoInfo, buttonBox);
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    /**
     * Proses pembayaran - sesuai flowchart "Bayar" -> "cek saldo" -> "proses pembayaran"
     */
    private void prosessPembayaran() {
        TransaksiResult result = sistem.prosesTransaksiLengkap(
            currentPengunjung, selectedJenisTiket, selectedKategori, jumlahOrang
        );
        
        updateSaldoDisplay();
        
        if (result.isBerhasil()) {
            showBuktiPembelian(result.getBuktiPembelian());
        } else {
            showSaldoTidakCukup(result);
        }
    }
    
    /**
     * Top up saldo - sesuai flowchart "Top up saldo"
     */
    private void showSaldoTidakCukup(TransaksiResult result) {
        contentArea.getChildren().clear();
        
        VBox card = createCard("Saldo Tidak Cukup", "");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        
        // Warning icon
        Circle warningCircle = new Circle(40);
        warningCircle.setFill(Color.web("#ffc107"));
        Label warningMark = new Label("!");
        warningMark.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: white;");
        StackPane warningIcon = new StackPane(warningCircle, warningMark);
        
        Label msgLabel = new Label(result.getPesan());
        msgLabel.getStyleClass().add("warning-message");
        msgLabel.setWrapText(true);
        msgLabel.setTextAlignment(TextAlignment.CENTER);
        
        int totalBayar = result.getTransaksi().getTotalBayar();
        int saldoKurang = totalBayar - currentJakCard.cekSaldo();
        
        Label infoLabel = new Label("Anda perlu top up minimal Rp " + String.format("%,d", saldoKurang));
        infoLabel.getStyleClass().add("info-label");
        
        TextField topUpField = createTextField("Jumlah Top Up (Rp)");
        topUpField.setMaxWidth(300);
        topUpField.setText(String.valueOf(saldoKurang));
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button cancelButton = createSecondaryButton("Batal");
        cancelButton.setOnAction(e -> showPilihJenisTiket());
        
        Button topUpButton = createPrimaryButton("Top Up & Bayar");
        topUpButton.setOnAction(e -> {
            try {
                int topUpAmount = Integer.parseInt(topUpField.getText().replace(".", "").replace(",", ""));
                sistem.topUpSaldo(currentJakCard, topUpAmount);
                updateSaldoDisplay();
                prosessPembayaran();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Masukkan angka yang valid!", Alert.AlertType.ERROR);
            }
        });
        
        buttonBox.getChildren().addAll(cancelButton, topUpButton);
        content.getChildren().addAll(warningIcon, msgLabel, infoLabel, topUpField, buttonBox);
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    /**
     * Cetak bukti - sesuai flowchart "cetak bukti &tiket" dan sequence diagram "Bukti Pembelian"
     */
    private void showBuktiPembelian(BuktiPembelian bukti) {
        contentArea.getChildren().clear();
        
        VBox card = createCard("Transaksi Berhasil!", "");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        
        // Success icon
        Circle successCircle = new Circle(40);
        successCircle.setFill(Color.web("#28a745"));
        Label checkMark = new Label("V");
        checkMark.setStyle("-fx-font-size: 40px; -fx-text-fill: white; -fx-font-weight: bold;");
        StackPane successIcon = new StackPane(successCircle, checkMark);
        
        // Receipt card
        VBox receiptCard = new VBox(15);
        receiptCard.getStyleClass().add("receipt-card");
        receiptCard.setAlignment(Pos.CENTER);
        receiptCard.setMaxWidth(400);
        
        Label receiptTitle = new Label("BUKTI PEMBELIAN TIKET MONAS");
        receiptTitle.getStyleClass().add("receipt-title");
        
        Separator sep1 = new Separator();
        
        GridPane detailGrid = new GridPane();
        detailGrid.setHgap(20);
        detailGrid.setVgap(10);
        detailGrid.setAlignment(Pos.CENTER);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        addReceiptRow(detailGrid, 0, "Kode Tiket", bukti.getKodeTiket());
        addReceiptRow(detailGrid, 1, "Tanggal", bukti.getTanggalCetak().format(formatter));
        addReceiptRow(detailGrid, 2, "Nama", currentPengunjung.getNama());
        addReceiptRow(detailGrid, 3, "Jenis Tiket", selectedJenisTiket);
        addReceiptRow(detailGrid, 4, "Kategori", selectedKategori);
        addReceiptRow(detailGrid, 5, "Jumlah", jumlahOrang + " tiket");
        
        Separator sep2 = new Separator();
        
        Label totalLabel = new Label("TOTAL: Rp " + String.format("%,d", bukti.getTransaksi().getTotalBayar()));
        totalLabel.getStyleClass().add("receipt-total");
        
        Label statusLabel = new Label("STATUS: " + bukti.getTransaksi().getStatus());
        statusLabel.getStyleClass().add("receipt-status");
        
        Label sisaSaldo = new Label("Sisa Saldo: Rp " + String.format("%,d", currentJakCard.cekSaldo()));
        sisaSaldo.getStyleClass().add("receipt-saldo");
        
        receiptCard.getChildren().addAll(receiptTitle, sep1, detailGrid, sep2, totalLabel, statusLabel, sisaSaldo);
        
        // Buttons - sesuai flowchart "Ingin beli lagi?"
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button buyAgainButton = createPrimaryButton("Beli Tiket Lagi");
        buyAgainButton.setOnAction(e -> showPilihJenisTiket());
        
        Button finishButton = createSecondaryButton("Selesai");
        finishButton.setOnAction(e -> showSelesai());
        
        buttonBox.getChildren().addAll(buyAgainButton, finishButton);
        content.getChildren().addAll(successIcon, receiptCard, buttonBox);
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    private void addReceiptRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("receipt-label");
        Label valueNode = new Label(": " + value);
        valueNode.getStyleClass().add("receipt-value");
        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }
    
    /**
     * Selesai - sesuai flowchart "Selesai"
     */
    private void showSelesai() {
        contentArea.getChildren().clear();
        
        VBox card = createCard("Terima Kasih!", "");
        
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        
        // Add Monas illustration
        StackPane monasIllustration = createLargeMonasIllustration();
        
        Label thankYouLabel = new Label("Terima kasih telah menggunakan\nSistem Pembelian Tiket MONAS");
        thankYouLabel.getStyleClass().add("thank-you-label");
        thankYouLabel.setTextAlignment(TextAlignment.CENTER);
        
        Label visitLabel = new Label("Selamat berkunjung ke Monumen Nasional!\nNikmati keindahan dan sejarah Indonesia.");
        visitLabel.getStyleClass().add("visit-label");
        visitLabel.setTextAlignment(TextAlignment.CENTER);
        
        Button newTransactionButton = createPrimaryButton("Transaksi Baru");
        newTransactionButton.setOnAction(e -> {
            currentJakCard = null;
            currentPengunjung = null;
            selectedJenisTiket = null;
            selectedKategori = null;
            jumlahOrang = 0;
            saldoLabel.setText("Rp 0");
            ((VBox) mainLayout.getTop()).getChildren().get(2).setVisible(false);
            showJakCardCheck();
        });
        
        content.getChildren().addAll(monasIllustration, thankYouLabel, visitLabel, newTransactionButton);
        ((VBox) card.getChildren().get(1)).getChildren().add(content);
        
        contentArea.getChildren().add(card);
        animateIn(card);
    }
    
    // Helper methods
    private int getHargaTiket(String jenisTiket, String kategori) {
        if (jenisTiket.equals("Puncak")) {
            switch (kategori) {
                case "Dewasa": return 24000;
                case "Mahasiswa": return 13000;
                case "Anak-anak": return 6000;
            }
        } else {
            switch (kategori) {
                case "Dewasa": return 8000;
                case "Mahasiswa": return 5000;
                case "Anak-anak": return 3000;
            }
        }
        return 0;
    }
    
    private void updateSaldoDisplay() {
        if (currentJakCard != null) {
            saldoLabel.setText("Rp " + String.format("%,d", currentJakCard.cekSaldo()));
        }
    }
    
    private VBox createCard(String title, String subtitle) {
        VBox card = new VBox(15);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(30));
        card.setMaxWidth(700);
        
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");
        
        header.getChildren().add(titleLabel);
        
        if (!subtitle.isEmpty()) {
            Label subtitleLabel = new Label(subtitle);
            subtitleLabel.getStyleClass().add("card-subtitle");
            header.getChildren().add(subtitleLabel);
        }
        
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        
        card.getChildren().addAll(header, contentBox);
        return card;
    }
    
    private Button createPrimaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("primary-button");
        return button;
    }
    
    private Button createSecondaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("secondary-button");
        return button;
    }
    
    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.getStyleClass().add("modern-text-field");
        textField.setFocusTraversable(true);
        textField.setEditable(true);
        textField.setMouseTransparent(false);
        return textField;
    }
    
    /**
     * Create a detailed Monas monument icon using JavaFX shapes
     */
    private StackPane createMonasIcon(double size) {
        StackPane container = new StackPane();
        
        double scale = size / 100.0;
        
        // Background circle (optional - for contrast)
        Circle bgCircle = new Circle(size / 2);
        bgCircle.setFill(Color.web("#FFD700", 0.2));
        
        // Create Monas structure using Group
        javafx.scene.Group monas = new javafx.scene.Group();
        
        // Base platform (bottom rectangle)
        Rectangle base = new Rectangle(60 * scale, 10 * scale);
        base.setFill(Color.web("#8B7355"));
        base.setTranslateX(-30 * scale);
        base.setTranslateY(40 * scale);
        
        // Lower platform
        Rectangle lowerPlatform = new Rectangle(50 * scale, 8 * scale);
        lowerPlatform.setFill(Color.web("#A0A0A0"));
        lowerPlatform.setTranslateX(-25 * scale);
        lowerPlatform.setTranslateY(32 * scale);
        
        // Main body (tower) - tapered rectangle using polygon
        Polygon tower = new Polygon();
        tower.getPoints().addAll(
            -12.0 * scale, 30.0 * scale,   // bottom left
            12.0 * scale, 30.0 * scale,    // bottom right
            8.0 * scale, -20.0 * scale,    // top right
            -8.0 * scale, -20.0 * scale    // top left
        );
        tower.setFill(Color.WHITE);
        tower.setStroke(Color.web("#D4D4D4"));
        tower.setStrokeWidth(1);
        
        // Upper section (narrower)
        Polygon upperSection = new Polygon();
        upperSection.getPoints().addAll(
            -6.0 * scale, -20.0 * scale,   // bottom left
            6.0 * scale, -20.0 * scale,    // bottom right
            4.0 * scale, -35.0 * scale,    // top right
            -4.0 * scale, -35.0 * scale    // top left
        );
        upperSection.setFill(Color.WHITE);
        upperSection.setStroke(Color.web("#D4D4D4"));
        upperSection.setStrokeWidth(1);
        
        // Gold flame/tip (the iconic golden flame)
        Polygon flame = new Polygon();
        flame.getPoints().addAll(
            0.0, -50.0 * scale,     // top point
            -5.0 * scale, -35.0 * scale,   // bottom left
            5.0 * scale, -35.0 * scale     // bottom right
        );
        flame.setFill(Color.web("#FFD700"));
        flame.setEffect(new DropShadow(5, Color.web("#FFA500")));
        
        // Add glow effect to flame
        Glow flameGlow = new Glow(0.5);
        flame.setEffect(flameGlow);
        
        // Windows/details on tower (decorative lines)
        Rectangle window1 = new Rectangle(4 * scale, 6 * scale);
        window1.setFill(Color.web("#4A90D9"));
        window1.setTranslateX(-2 * scale);
        window1.setTranslateY(5 * scale);
        
        Rectangle window2 = new Rectangle(4 * scale, 6 * scale);
        window2.setFill(Color.web("#4A90D9"));
        window2.setTranslateX(-2 * scale);
        window2.setTranslateY(15 * scale);
        
        // Observation deck ring
        Rectangle deck = new Rectangle(20 * scale, 3 * scale);
        deck.setFill(Color.web("#C0C0C0"));
        deck.setTranslateX(-10 * scale);
        deck.setTranslateY(-22 * scale);
        
        monas.getChildren().addAll(base, lowerPlatform, tower, upperSection, deck, window1, window2, flame);
        
        container.getChildren().addAll(bgCircle, monas);
        return container;
    }
    
    /**
     * Create a large Monas illustration for welcome/finish screens
     */
    private StackPane createLargeMonasIllustration() {
        StackPane container = new StackPane();
        container.setPrefSize(200, 250);
        
        // Sky background
        Rectangle sky = new Rectangle(200, 200);
        sky.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#87CEEB")),
            new Stop(1, Color.web("#E0F4FF"))
        ));
        sky.setArcWidth(20);
        sky.setArcHeight(20);
        
        // Ground
        Rectangle ground = new Rectangle(200, 50);
        ground.setFill(Color.web("#90EE90"));
        ground.setTranslateY(75);
        
        // Create large Monas
        javafx.scene.Group monas = new javafx.scene.Group();
        
        // Base plaza
        Rectangle plaza = new Rectangle(150, 20);
        plaza.setFill(Color.web("#D2B48C"));
        plaza.setTranslateX(-75);
        plaza.setTranslateY(55);
        
        // Steps
        Rectangle step1 = new Rectangle(120, 10);
        step1.setFill(Color.web("#A0A0A0"));
        step1.setTranslateX(-60);
        step1.setTranslateY(45);
        
        Rectangle step2 = new Rectangle(100, 8);
        step2.setFill(Color.web("#B0B0B0"));
        step2.setTranslateX(-50);
        step2.setTranslateY(37);
        
        // Main tower
        Polygon mainTower = new Polygon();
        mainTower.getPoints().addAll(
            -25.0, 35.0,
            25.0, 35.0,
            18.0, -50.0,
            -18.0, -50.0
        );
        mainTower.setFill(Color.WHITE);
        mainTower.setStroke(Color.web("#C0C0C0"));
        mainTower.setStrokeWidth(2);
        
        // Upper tower
        Polygon upperTower = new Polygon();
        upperTower.getPoints().addAll(
            -12.0, -50.0,
            12.0, -50.0,
            8.0, -75.0,
            -8.0, -75.0
        );
        upperTower.setFill(Color.WHITE);
        upperTower.setStroke(Color.web("#C0C0C0"));
        
        // Observation deck
        Rectangle obsDeck = new Rectangle(40, 6);
        obsDeck.setFill(Color.web("#A0A0A0"));
        obsDeck.setTranslateX(-20);
        obsDeck.setTranslateY(-53);
        
        // Golden flame
        Polygon goldenFlame = new Polygon();
        goldenFlame.getPoints().addAll(
            0.0, -100.0,
            -10.0, -75.0,
            10.0, -75.0
        );
        goldenFlame.setFill(Color.web("#FFD700"));
        
        // Flame glow effect
        DropShadow flameGlow = new DropShadow();
        flameGlow.setColor(Color.web("#FFA500"));
        flameGlow.setRadius(15);
        flameGlow.setSpread(0.3);
        goldenFlame.setEffect(flameGlow);
        
        // Windows
        for (int i = 0; i < 3; i++) {
            Rectangle win = new Rectangle(8, 12);
            win.setFill(Color.web("#4A90D9"));
            win.setTranslateX(-4);
            win.setTranslateY(-10 + (i * 18));
            monas.getChildren().add(win);
        }
        
        monas.getChildren().addAll(plaza, step1, step2, mainTower, upperTower, obsDeck, goldenFlame);
        monas.setTranslateY(20);
        
        // Add some clouds
        Circle cloud1 = new Circle(15);
        cloud1.setFill(Color.WHITE);
        cloud1.setTranslateX(-70);
        cloud1.setTranslateY(-70);
        
        Circle cloud2 = new Circle(12);
        cloud2.setFill(Color.WHITE);
        cloud2.setTranslateX(-55);
        cloud2.setTranslateY(-72);
        
        Circle cloud3 = new Circle(10);
        cloud3.setFill(Color.WHITE);
        cloud3.setTranslateX(60);
        cloud3.setTranslateY(-60);
        
        Circle cloud4 = new Circle(13);
        cloud4.setFill(Color.WHITE);
        cloud4.setTranslateX(75);
        cloud4.setTranslateY(-58);
        
        container.getChildren().addAll(sky, ground, cloud1, cloud2, cloud3, cloud4, monas);
        
        return container;
    }
    
    private void animateIn(Node node) {
        node.setOpacity(0);
        node.setTranslateY(20);
        
        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        
        TranslateTransition translate = new TranslateTransition(Duration.millis(300), node);
        translate.setFromY(20);
        translate.setToY(0);
        
        ParallelTransition parallel = new ParallelTransition(fade, translate);
        parallel.play();
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
