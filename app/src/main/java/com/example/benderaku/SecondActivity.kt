package com.example.benderaku

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data satu benua
data class Benua(
    val nama: String,
    val emoji: String,
    val contohNegara: String,
    val warna: Color
)

// Daftar benua yang punya negara (Antartika tidak punya negara, jadi tidak dimasukkan)
val daftarBenua = listOf(
    Benua("Asia", "🌏", "🇮🇩 🇯🇵 🇮🇳 🇨🇳 🇰🇷", Color(0xFFE53935)),
    Benua("Eropa", "🏰", "🇫🇷 🇩🇪 🇮🇹 🇪🇸 🇬🇧", Color(0xFF1E88E5)),
    Benua("Afrika", "🦁", "🇪🇬 🇳🇬 🇿🇦 🇰🇪 🇲🇦", Color(0xFFF9A825)),
    Benua("Amerika Utara", "🗽", "🇺🇸 🇨🇦 🇲🇽 🇨🇺 🇵🇦", Color(0xFF43A047)),
    Benua("Amerika Selatan", "🌴", "🇧🇷 🇦🇷 🇨🇱 🇵🇪 🇨🇴", Color(0xFF8E24AA)),
    Benua("Oseania", "🦘", "🇦🇺 🇳🇿 🇫🇯 🇵🇬 🇼🇸", Color(0xFF00ACC1))
)

class SecondActivity : ComponentActivity() {

    companion object {
        const val EXTRA_BENUA = "extra_benua"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil data dari halaman login
        val nama = intent.getStringExtra(MainActivity.EXTRA_NAMA) ?: "Teman"
        val email = intent.getStringExtra(MainActivity.EXTRA_EMAIL) ?: "-"

        setContent {
            MaterialTheme {
                BenuaScreen(
                    nama = nama,
                    email = email,
                    onBenuaClick = { benua ->
                        // Kirim nama benua ke halaman berikutnya (ThirdActivity)
                        val intent = Intent(this, ThirdActivity::class.java).apply {
                            putExtra(MainActivity.EXTRA_NAMA, nama)
                            putExtra(MainActivity.EXTRA_EMAIL, email)
                            putExtra(EXTRA_BENUA, benua.nama)
                        }
                        startActivity(intent)
                    },
                    onLogout = {
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun BenuaScreen(
    nama: String,
    email: String,
    onBenuaClick: (Benua) -> Unit,
    onLogout: () -> Unit
) {
    val bgBrush = Brush.verticalGradient(
        listOf(Color(0xFF0D47A1), Color(0xFF1976D2), Color(0xFF64B5F6), Color(0xFFE3F2FD))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: sapaan + info user (memenuhi 2 kolom)
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Halo, $nama! 👋",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0D47A1)
                        )
                        Text(
                            text = email,
                            fontSize = 12.sp,
                            color = Color(0xFF78909C)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Pilih benua yang ingin kamu pelajari bendera negaranya.",
                            fontSize = 14.sp,
                            color = Color(0xFF455A64)
                        )
                    }
                }
            }

            // Kartu per benua
            items(daftarBenua) { benua ->
                BenuaCard(benua = benua, onClick = { onBenuaClick(benua) })
            }

            // Tombol logout (memenuhi 2 kolom)
            item(span = { GridItemSpan(2) }) {
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "⬅ Keluar / Ganti Akun",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F)
                    )
                }
            }
        }
    }
}

@Composable
fun BenuaCard(benua: Benua, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = benua.warna),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = benua.emoji, fontSize = 40.sp)

            Spacer(Modifier.height(6.dp))

            Text(
                text = benua.nama,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = benua.contohNegara,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}