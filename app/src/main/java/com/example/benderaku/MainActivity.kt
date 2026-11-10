package com.example.benderaku

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions

class MainActivity : ComponentActivity() {

    companion object {
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_EMAIL = "extra_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen(
                    onLoginSuccess = { nama, email ->
                        Toast.makeText(
                            this@MainActivity,
                            "Login berhasil, selamat datang $nama! 🌍",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@MainActivity, SecondActivity::class.java).apply {
                            putExtra(EXTRA_NAMA, nama)
                            putExtra(EXTRA_EMAIL, email)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

// ---------- Fungsi validasi (return pesan error, atau null kalau valid) ----------

private fun validasiNama(nama: String): String? = when {
    nama.isBlank() -> "Nama tidak boleh kosong"
    nama.trim().length < 2 -> "Nama minimal 2 karakter"
    else -> null
}

private fun validasiEmail(email: String): String? = when {
    email.isBlank() -> "Email tidak boleh kosong"
    !email.contains("@") -> "Email tidak valid, harus mengandung '@'"
    else -> null
}

// ---------- Tampilan ----------

@Composable
fun LoginScreen(onLoginSuccess: (String, String) -> Unit) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Error baru tampil setelah field disentuh / tombol ditekan
    var namaTouched by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }

    val namaError = if (namaTouched) validasiNama(nama) else null
    val emailError = if (emailTouched) validasiEmail(email) else null

    // Warna tema: biru laut + aksen merah putih
    val bgBrush = Brush.verticalGradient(
        listOf(Color(0xFF0D47A1), Color(0xFF1976D2), Color(0xFF64B5F6), Color(0xFFE3F2FD))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
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
                    text = "🇮🇩 🇯🇵 🇧🇷 🇫🇷 🇺🇸",
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "KENALI BENDERA DUNIA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0D47A1),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Login dulu untuk mulai menebak bendera!",
                    fontSize = 13.sp,
                    color = Color(0xFF546E7A),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                // Input Nama
                OutlinedTextField(
                    value = nama,
                    onValueChange = {
                        nama = it
                        namaTouched = true // validasi langsung saat mengetik
                    },
                    label = { Text("Nama") },
                    placeholder = { Text("Minimal 2 karakter") },
                    singleLine = true,
                    isError = namaError != null, // border & label otomatis merah
                    supportingText = { namaError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(8.dp))

                // Input Email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailTouched = true
                    },
                    label = { Text("Email") },
                    placeholder = { Text("contoh: budi@gmail.com") },
                    singleLine = true,
                    isError = emailError != null,
                    supportingText = { emailError?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(20.dp))

                // Tombol Login
                Button(
                    onClick = {
                        // Tandai semua field supaya error muncul sekaligus
                        namaTouched = true
                        emailTouched = true

                        if (validasiNama(nama) == null && validasiEmail(email) == null) {
                            onLoginSuccess(nama.trim(), email.trim())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text(
                        text = "MULAI 🌍",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}