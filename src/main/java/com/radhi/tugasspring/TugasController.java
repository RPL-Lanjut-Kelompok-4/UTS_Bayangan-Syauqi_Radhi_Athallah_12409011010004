package com.radhi.tugasspring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Controller
public class TugasController {

    private String rupiah(BigDecimal nilai) {
        return NumberFormat
                .getCurrencyInstance(Locale.forLanguageTag("id-ID"))
                .format(nilai);
    }

    @GetMapping("/")
    public String beranda() {
        return "redirect:/john";
    }

    // Menampilkan halaman John Travolta.
    @GetMapping("/john")
    public String halamanJohn(Model model) {
        model.addAttribute("jam", 52);
        model.addAttribute("tarif", 15000);
        model.addAttribute("pengeluaran", 600000);
        return "john";
    }

    // Memproses perhitungan gaji dan tabungan.
    @PostMapping("/john")
    public String hitungJohn(
            @RequestParam("jam") BigDecimal jam,
            @RequestParam("tarif") BigDecimal tarif,
            @RequestParam("pengeluaran") BigDecimal pengeluaran,
            Model model) {

        model.addAttribute("jam", jam);
        model.addAttribute("tarif", tarif);
        model.addAttribute("pengeluaran", pengeluaran);

        if (jam.signum() < 0
                || tarif.signum() < 0
                || pengeluaran.signum() < 0) {

            model.addAttribute("error", "Input tidak boleh negatif.");
            return "john";
        }

        BigDecimal batas = new BigDecimal("40");

        BigDecimal jamNormal = jam.min(batas);
        BigDecimal jamLembur = jam.subtract(batas).max(BigDecimal.ZERO);
        BigDecimal tarifLembur = tarif.multiply(new BigDecimal("1.5"));

        BigDecimal gajiNormal = jamNormal.multiply(tarif);
        BigDecimal gajiLembur = jamLembur.multiply(tarifLembur);
        BigDecimal totalGaji = gajiNormal.add(gajiLembur);
        BigDecimal selisih = totalGaji.subtract(pengeluaran);

        String status;
        String keterangan;

        if (selisih.signum() > 0) {
            status = "bisa menabung";
            keterangan = "Tabungan: " + rupiah(selisih);
        } else if (selisih.signum() == 0) {
            status = "tidak bisa menabung";
            keterangan = "Pemasukan sama dengan pengeluaran.";
        } else {
            status = "cari tambahan";
            keterangan = "Kekurangan: " + rupiah(selisih.abs());
        }

        model.addAttribute("hasil", true);
        model.addAttribute("jamNormal", jamNormal);
        model.addAttribute("jamLembur", jamLembur);
        model.addAttribute("gajiNormal", rupiah(gajiNormal));
        model.addAttribute("gajiLembur", rupiah(gajiLembur));
        model.addAttribute("totalGaji", rupiah(totalGaji));
        model.addAttribute("status", status);
        model.addAttribute("keterangan", keterangan);

        return "john";
    }

    // Menampilkan halaman persamaan kuadrat.
    @GetMapping("/kuadrat")
    public String halamanKuadrat(Model model) {
        model.addAttribute("a", 1);
        model.addAttribute("b", -5);
        model.addAttribute("c", 6);
        return "kuadrat";
    }

    // Memproses perhitungan persamaan kuadrat.
    @PostMapping("/kuadrat")
    public String hitungKuadrat(
            @RequestParam("a") double a,
            @RequestParam("b") double b,
            @RequestParam("c") double c,
            Model model) {

        model.addAttribute("a", a);
        model.addAttribute("b", b);
        model.addAttribute("c", c);

        if (!Double.isFinite(a)
                || !Double.isFinite(b)
                || !Double.isFinite(c)) {

            model.addAttribute("error", "Masukkan angka yang valid.");
            return "kuadrat";
        }

        if (a == 0) {
            model.addAttribute(
                    "error",
                    "a tidak boleh 0 karena bukan persamaan kuadrat."
            );
            return "kuadrat";
        }

        double d = b * b - 4 * a * c;
        double penyebut = 2 * a;

        if (!Double.isFinite(d) || !Double.isFinite(penyebut)) {
            model.addAttribute("error", "Nilai terlalu besar.");
            return "kuadrat";
        }

        String jenis;
        String x1;
        String x2;

        if (d > 0) {
            jenis = "Dua akar real berbeda";
            x1 = Double.toString((-b + Math.sqrt(d)) / penyebut);
            x2 = Double.toString((-b - Math.sqrt(d)) / penyebut);
        } else if (d == 0) {
            jenis = "Akar real kembar";
            x1 = Double.toString(-b / penyebut);
            x2 = x1;
        } else {
            jenis = "Dua akar kompleks, tidak memiliki akar real";

            double real = -b / penyebut;
            double imajiner = Math.sqrt(-d) / Math.abs(penyebut);

            x1 = real + " + " + imajiner + "i";
            x2 = real + " - " + imajiner + "i";
        }

        model.addAttribute("hasil", true);
        model.addAttribute("d", d);
        model.addAttribute("jenis", jenis);
        model.addAttribute("x1", x1);
        model.addAttribute("x2", x2);

        return "kuadrat";
    }
}