package com.example.benderaku

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Data ----------

// kode = kode negara ISO 2 huruf, dipakai untuk membuat emoji bendera otomatis
data class Negara(val nama: String, val kode: String) {
    val bendera: String
        get() = kode.uppercase()
            .map { String(Character.toChars(0x1F1E6 + (it - 'A'))) }
            .joinToString("")
}

val dataNegara: Map<String, List<Negara>> = mapOf(

    "Asia" to listOf(
        Negara("Afghanistan", "AF"), Negara("Arab Saudi", "SA"), Negara("Armenia", "AM"),
        Negara("Azerbaijan", "AZ"), Negara("Bahrain", "BH"), Negara("Bangladesh", "BD"),
        Negara("Bhutan", "BT"), Negara("Brunei Darussalam", "BN"), Negara("Filipina", "PH"),
        Negara("Georgia", "GE"), Negara("India", "IN"), Negara("Indonesia", "ID"),
        Negara("Irak", "IQ"), Negara("Iran", "IR"), Negara("Israel", "IL"),
        Negara("Jepang", "JP"), Negara("Kamboja", "KH"), Negara("Kazakhstan", "KZ"),
        Negara("Kirgistan", "KG"), Negara("Korea Selatan", "KR"), Negara("Korea Utara", "KP"),
        Negara("Kuwait", "KW"), Negara("Laos", "LA"), Negara("Lebanon", "LB"),
        Negara("Malaysia", "MY"), Negara("Maladewa", "MV"), Negara("Mongolia", "MN"),
        Negara("Myanmar", "MM"), Negara("Nepal", "NP"), Negara("Oman", "OM"),
        Negara("Pakistan", "PK"), Negara("Palestina", "PS"), Negara("Qatar", "QA"),
        Negara("Singapura", "SG"), Negara("Siprus", "CY"), Negara("Sri Lanka", "LK"),
        Negara("Suriah", "SY"), Negara("Tajikistan", "TJ"), Negara("Thailand", "TH"),
        Negara("Timor Leste", "TL"), Negara("Tiongkok", "CN"), Negara("Turkmenistan", "TM"),
        Negara("Turki", "TR"), Negara("Uni Emirat Arab", "AE"), Negara("Uzbekistan", "UZ"),
        Negara("Vietnam", "VN"), Negara("Yaman", "YE"), Negara("Yordania", "JO")
    ),

    "Eropa" to listOf(
        Negara("Albania", "AL"), Negara("Andorra", "AD"), Negara("Austria", "AT"),
        Negara("Belanda", "NL"), Negara("Belarus", "BY"), Negara("Belgia", "BE"),
        Negara("Bosnia dan Herzegovina", "BA"), Negara("Bulgaria", "BG"), Negara("Ceko", "CZ"),
        Negara("Denmark", "DK"), Negara("Estonia", "EE"), Negara("Finlandia", "FI"),
        Negara("Hungaria", "HU"), Negara("Inggris", "GB"), Negara("Irlandia", "IE"),
        Negara("Islandia", "IS"), Negara("Italia", "IT"), Negara("Jerman", "DE"),
        Negara("Kosovo", "XK"), Negara("Kroasia", "HR"), Negara("Latvia", "LV"),
        Negara("Liechtenstein", "LI"), Negara("Lituania", "LT"), Negara("Luksemburg", "LU"),
        Negara("Makedonia Utara", "MK"), Negara("Malta", "MT"), Negara("Moldova", "MD"),
        Negara("Monako", "MC"), Negara("Montenegro", "ME"), Negara("Norwegia", "NO"),
        Negara("Polandia", "PL"), Negara("Portugal", "PT"), Negara("Prancis", "FR"),
        Negara("Rumania", "RO"), Negara("Rusia", "RU"), Negara("San Marino", "SM"),
        Negara("Serbia", "RS"), Negara("Slovakia", "SK"), Negara("Slovenia", "SI"),
        Negara("Spanyol", "ES"), Negara("Swedia", "SE"), Negara("Swiss", "CH"),
        Negara("Ukraina", "UA"), Negara("Vatikan", "VA"), Negara("Yunani", "GR")
    ),

    "Afrika" to listOf(
        Negara("Afrika Selatan", "ZA"), Negara("Aljazair", "DZ"), Negara("Angola", "AO"),
        Negara("Benin", "BJ"), Negara("Botswana", "BW"), Negara("Burkina Faso", "BF"),
        Negara("Burundi", "BI"), Negara("Chad", "TD"), Negara("Djibouti", "DJ"),
        Negara("Eritrea", "ER"), Negara("Eswatini", "SZ"), Negara("Etiopia", "ET"),
        Negara("Gabon", "GA"), Negara("Gambia", "GM"), Negara("Ghana", "GH"),
        Negara("Guinea", "GN"), Negara("Guinea-Bissau", "GW"), Negara("Guinea Khatulistiwa", "GQ"),
        Negara("Kamerun", "CM"), Negara("Kenya", "KE"), Negara("Komoro", "KM"),
        Negara("Kongo", "CG"), Negara("Kongo (Republik Demokratik)", "CD"), Negara("Lesotho", "LS"),
        Negara("Liberia", "LR"), Negara("Libya", "LY"), Negara("Madagaskar", "MG"),
        Negara("Malawi", "MW"), Negara("Mali", "ML"), Negara("Maroko", "MA"),
        Negara("Mauritania", "MR"), Negara("Mauritius", "MU"), Negara("Mesir", "EG"),
        Negara("Mozambik", "MZ"), Negara("Namibia", "NA"), Negara("Niger", "NE"),
        Negara("Nigeria", "NG"), Negara("Pantai Gading", "CI"), Negara("Republik Afrika Tengah", "CF"),
        Negara("Rwanda", "RW"), Negara("Sao Tome dan Principe", "ST"), Negara("Senegal", "SN"),
        Negara("Seychelles", "SC"), Negara("Sierra Leone", "SL"), Negara("Somalia", "SO"),
        Negara("Sudan", "SD"), Negara("Sudan Selatan", "SS"), Negara("Tanjung Verde", "CV"),
        Negara("Tanzania", "TZ"), Negara("Togo", "TG"), Negara("Tunisia", "TN"),
        Negara("Uganda", "UG"), Negara("Zambia", "ZM"), Negara("Zimbabwe", "ZW")
    ),

    "Amerika Utara" to listOf(
        Negara("Amerika Serikat", "US"), Negara("Antigua dan Barbuda", "AG"), Negara("Bahama", "BS"),
        Negara("Barbados", "BB"), Negara("Belize", "BZ"), Negara("Dominika", "DM"),
        Negara("El Salvador", "SV"), Negara("Grenada", "GD"), Negara("Guatemala", "GT"),
        Negara("Haiti", "HT"), Negara("Honduras", "HN"), Negara("Jamaika", "JM"),
        Negara("Kanada", "CA"), Negara("Kosta Rika", "CR"), Negara("Kuba", "CU"),
        Negara("Meksiko", "MX"), Negara("Nikaragua", "NI"), Negara("Panama", "PA"),
        Negara("Republik Dominika", "DO"), Negara("Saint Kitts dan Nevis", "KN"),
        Negara("Saint Lucia", "LC"), Negara("Saint Vincent dan Grenadines", "VC"),
        Negara("Trinidad dan Tobago", "TT")
    ),

    "Amerika Selatan" to listOf(
        Negara("Argentina", "AR"), Negara("Bolivia", "BO"), Negara("Brasil", "BR"),
        Negara("Chili", "CL"), Negara("Ekuador", "EC"), Negara("Guyana", "GY"),
        Negara("Kolombia", "CO"), Negara("Paraguay", "PY"), Negara("Peru", "PE"),
        Negara("Suriname", "SR"), Negara("Uruguay", "UY"), Negara("Venezuela", "VE")
    ),

    "Oseania" to listOf(
        Negara("Australia", "AU"), Negara("Fiji", "FJ"), Negara("Kepulauan Marshall", "MH"),
        Negara("Kepulauan Solomon", "SB"), Negara("Kiribati", "KI"), Negara("Mikronesia", "FM"),
        Negara("Nauru", "NR"), Negara("Palau", "PW"), Negara("Papua Nugini", "PG"),
        Negara("Samoa", "WS"), Negara("Selandia Baru", "NZ"), Negara("Tonga", "TO"),
        Negara("Tuvalu", "TV"), Negara("Vanuatu", "VU")
    )
)

// ---------- Activity ----------

class ThirdActivity : ComponentActivity() {

    companion object {
        const val EXTRA_NEGARA = "extra_negara"
        const val EXTRA_KODE = "extra_kode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nama = intent.getStringExtra(MainActivity.EXTRA_NAMA) ?: "Teman"
        val email = intent.getStringExtra(MainActivity.EXTRA_EMAIL) ?: "-"
        val benua = intent.getStringExtra(SecondActivity.EXTRA_BENUA) ?: "Asia"

        // Daftar negara diurutkan A-Z
        val listNegara = (dataNegara[benua] ?: emptyList()).sortedBy { it.nama }

        setContent {
            MaterialTheme {
                NegaraScreen(
                    benua = benua,
                    listNegara = listNegara,
                    onBack = { finish() },
                    onNegaraClick = { negara ->
                        val i = Intent(this, FourthActivity::class.java).apply {
                            putExtra(MainActivity.EXTRA_NAMA, nama)
                            putExtra(MainActivity.EXTRA_EMAIL, email)
                            putExtra(SecondActivity.EXTRA_BENUA, benua)
                            putExtra(EXTRA_NEGARA, negara.nama)
                            putExtra(EXTRA_KODE, negara.kode)
                        }
                        startActivity(i)
                    }
                )
            }
        }
    }
}

// ---------- Tampilan ----------

@Composable
fun NegaraScreen(
    benua: String,
    listNegara: List<Negara>,
    onBack: () -> Unit,
    onNegaraClick: (Negara) -> Unit
) {
    var query by remember { mutableStateOf("") }

    // Ambil warna & emoji benua dari daftarBenua di SecondActivity
    val info = daftarBenua.find { it.nama == benua }
    val warnaBenua = info?.warna ?: Color(0xFF1976D2)
    val emojiBenua = info?.emoji ?: "🌍"

    val hasil = listNegara.filter { it.nama.contains(query.trim(), ignoreCase = true) }

    val bgBrush = Brush.verticalGradient(
        listOf(Color(0xFF0D47A1), Color(0xFF1976D2), Color(0xFF64B5F6), Color(0xFFE3F2FD))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        // Header benua
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = warnaBenua),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = emojiBenua, fontSize = 38.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = benua,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "${listNegara.size} negara",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Kolom pencarian
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Cari negara...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(Modifier.height(12.dp))

        // Daftar negara
        if (hasil.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Negara tidak ditemukan 🔍",
                    color = Color(0xFF0D47A1),
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(hasil) { negara ->
                    NegaraItem(negara = negara, onClick = { onNegaraClick(negara) })
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Tombol kembali
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Text(
                text = "⬅ Kembali ke Pilihan Benua",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F)
            )
        }

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun NegaraItem(negara: Negara, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = negara.bendera, fontSize = 40.sp)
            Spacer(Modifier.width(16.dp))
            Text(
                text = negara.nama,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238),
                modifier = Modifier.weight(1f)
            )
            Text(text = "›", fontSize = 26.sp, color = Color(0xFF90A4AE))
        }
    }
}