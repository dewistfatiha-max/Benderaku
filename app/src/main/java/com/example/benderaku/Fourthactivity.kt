package com.example.benderaku

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val TOTAL_SOAL = 10

// Satu soal kuis: negara yang benar + 4 pilihan jawaban
data class Soal(val benar: Negara, val opsi: List<Negara>)

private fun buatSoal(daftar: List<Negara>): Soal {
    val benar = daftar.random()
    val pengecoh = (daftar - benar).shuffled().take(3)
    return Soal(benar, (pengecoh + benar).shuffled())
}

// ---------- Activity ----------

class FourthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nama = intent.getStringExtra(MainActivity.EXTRA_NAMA) ?: "Teman"
        val email = intent.getStringExtra(MainActivity.EXTRA_EMAIL) ?: "-"
        val benua = intent.getStringExtra(SecondActivity.EXTRA_BENUA) ?: "Asia"
        val namaNegara = intent.getStringExtra(ThirdActivity.EXTRA_NEGARA) ?: "-"
        val kode = intent.getStringExtra(ThirdActivity.EXTRA_KODE) ?: "ID"

        val negara = Negara(namaNegara, kode)

        setContent {
            MaterialTheme {
                DetailScreen(
                    nama = nama,
                    negara = negara,
                    benua = benua,
                    onBack = { finish() },
                    onHome = {
                        val i = Intent(this, SecondActivity::class.java).apply {
                            putExtra(MainActivity.EXTRA_NAMA, nama)
                            putExtra(MainActivity.EXTRA_EMAIL, email)
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(i)
                        finish()
                    }
                )
            }
        }
    }
}

// ---------- Tampilan utama ----------

@Composable
fun DetailScreen(
    nama: String,
    negara: Negara,
    benua: String,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    var kuisAktif by remember { mutableStateOf(false) }

    val info = daftarBenua.find { it.nama == benua }
    val warnaBenua = info?.warna ?: Color(0xFF1976D2)
    val emojiBenua = info?.emoji ?: "🌍"

    val bgBrush = Brush.verticalGradient(
        listOf(Color(0xFF0D47A1), Color(0xFF1976D2), Color(0xFF64B5F6), Color(0xFFE3F2FD))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!kuisAktif) {
            // ===== MODE DETAIL NEGARA =====
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hai $nama, ini bendera:",
                        fontSize = 13.sp,
                        color = Color(0xFF78909C)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(text = negara.bendera, fontSize = 120.sp)

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = negara.nama,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0D47A1),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(16.dp))

                    // Info singkat
                    InfoBaris("Benua", "$emojiBenua $benua", warnaBenua)
                    Spacer(Modifier.height(8.dp))
                    InfoBaris("Kode Negara", negara.kode.uppercase(), Color(0xFF455A64))
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { kuisAktif = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text(
                    text = "🎯 MAIN KUIS BENDERA $benua".uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(10.dp))

            TombolPutih("⬅ Kembali ke Daftar Negara", onBack)
            Spacer(Modifier.height(10.dp))
            TombolPutih("🏠 Ke Pilihan Benua", onHome)

        } else {
            // ===== MODE KUIS =====
            KuisBendera(
                benua = benua,
                warnaBenua = warnaBenua,
                onTutup = { kuisAktif = false }
            )
        }
    }
}

@Composable
fun InfoBaris(label: String, nilai: String, warna: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F7FA), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF78909C))
        Text(text = nilai, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = warna)
    }
}

@Composable
fun TombolPutih(teks: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
    ) {
        Text(text = teks, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
    }
}

// ---------- Kuis ----------

@Composable
fun KuisBendera(benua: String, warnaBenua: Color, onTutup: () -> Unit) {
    val daftar = remember(benua) { dataNegara[benua] ?: emptyList() }

    var nomor by remember { mutableIntStateOf(1) }
    var skor by remember { mutableIntStateOf(0) }
    var soal by remember { mutableStateOf(buatSoal(daftar)) }
    var dipilih by remember { mutableStateOf<Negara?>(null) }
    var selesai by remember { mutableStateOf(false) }

    fun mulaiLagi() {
        nomor = 1
        skor = 0
        soal = buatSoal(daftar)
        dipilih = null
        selesai = false
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!selesai) {
                // Header kuis
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Soal $nomor/$TOTAL_SOAL",
                        fontWeight = FontWeight.Bold,
                        color = warnaBenua
                    )
                    Text(
                        text = "Skor: $skor",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }

                Spacer(Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { (nomor - 1) / TOTAL_SOAL.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                    color = warnaBenua
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Bendera negara apakah ini?",
                    fontSize = 15.sp,
                    color = Color(0xFF455A64)
                )

                Text(text = soal.benar.bendera, fontSize = 100.sp)

                Spacer(Modifier.height(12.dp))

                // 4 pilihan jawaban
                soal.opsi.forEach { opsi ->
                    val warnaCard = when {
                        dipilih == null -> Color(0xFFF5F7FA)
                        opsi == soal.benar -> Color(0xFFA5D6A7)   // jawaban benar = hijau
                        opsi == dipilih -> Color(0xFFEF9A9A)      // pilihan salah = merah
                        else -> Color(0xFFF5F7FA)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(enabled = dipilih == null) {
                                dipilih = opsi
                                if (opsi == soal.benar) skor++
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = warnaCard)
                    ) {
                        Text(
                            text = opsi.nama,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color(0xFF263238),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Feedback + tombol lanjut
                if (dipilih != null) {
                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = if (dipilih == soal.benar) "✅ Benar!"
                        else "❌ Salah! Jawaban: ${soal.benar.nama}",
                        fontWeight = FontWeight.Bold,
                        color = if (dipilih == soal.benar) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (nomor == TOTAL_SOAL) {
                                selesai = true
                            } else {
                                nomor++
                                soal = buatSoal(daftar)
                                dipilih = null
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = warnaBenua)
                    ) {
                        Text(
                            text = if (nomor == TOTAL_SOAL) "Lihat Hasil 🏆" else "Lanjut ➜",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            } else {
                // Hasil akhir
                val komentar = when {
                    skor == TOTAL_SOAL -> "Sempurna! Kamu jagoan bendera! 🏆"
                    skor >= 7 -> "Keren! Pengetahuanmu hebat! 🎉"
                    skor >= 4 -> "Lumayan! Yuk belajar lagi 💪"
                    else -> "Jangan menyerah, coba lagi ya! 📚"
                }

                Text(text = "🏁 Kuis Selesai!", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = warnaBenua)
                Spacer(Modifier.height(12.dp))
                Text(text = "$skor / $TOTAL_SOAL", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0D47A1))
                Spacer(Modifier.height(8.dp))
                Text(text = komentar, textAlign = TextAlign.Center, color = Color(0xFF455A64))
                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = { mulaiLagi() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("🔄 Main Lagi", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }

    Spacer(Modifier.height(14.dp))

    TombolPutih("⬅ Kembali ke Detail Negara", onTutup)
}