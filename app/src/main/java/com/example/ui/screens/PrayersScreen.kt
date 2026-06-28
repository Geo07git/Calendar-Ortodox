package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class Prayer(val title: String, val content: String)

val prayersList = listOf(
    Prayer(
        title = "Tatăl Nostru",
        content = "Tatăl nostru, Care ești în ceruri, sfințească-se numele Tău, vie împărăția Ta, facă-se voia Ta, precum în cer așa și pe pământ. Pâinea noastră cea de toate zilele dă-ne-o nouă astăzi și ne iartă nouă greșelile noastre, precum și noi iertăm greșiților noștri. Și nu ne duce pe noi în ispită, ci ne izbăvește de cel rău. Amin."
    ),
    Prayer(
        title = "Crezul",
        content = "Cred într-Unul Dumnezeu, Tatăl Atotțiitorul, Făcătorul cerului și al pământului, văzutelor tuturor și nevăzutelor.\nȘi într-Unul Domn Iisus Hristos, Fiul lui Dumnezeu, Unul-Născut, Care din Tatăl S-a născut mai înainte de toți vecii. Lumină din Lumină, Dumnezeu adevărat din Dumnezeu adevărat, născut, nu făcut, Cel de o ființă cu Tatăl, prin Care toate s-au făcut.\nCare pentru noi oamenii și pentru a noastră mântuire S-a pogorât din ceruri și S-a întrupat de la Duhul Sfânt și din Maria Fecioara și S-a făcut om...\nȘi întru Duhul Sfânt, Domnul de viață Făcătorul, Care de la Tatăl purcede, Cel ce împreună cu Tatăl și cu Fiul este închinat și slăvit, Care a grăit prin prooroci.\nÎntr-una, sfântă, sobornicească și apostolească Biserică.\nMărturisesc un botez spre iertarea păcatelor.\nAștept învierea morților. Și viața veacului ce va să fie. Amin."
    ),
    Prayer(
        title = "Rugăciunea Inimii",
        content = "Doamne Iisuse Hristoase, Fiul lui Dumnezeu, miluiește-mă pe mine, păcătosul."
    ),
    Prayer(
        title = "Psalmul 50",
        content = "Miluiește-mă, Dumnezeule, după marea mila Ta și după mulțimea îndurărilor Tale, șterge fărădelegea mea.\nMai vârtos mă spală de fărădelegea mea și de păcatul meu mă curățește.\nCă fărădelegea mea eu o cunosc și păcatul meu înaintea mea este pururea.\nȚie unuia am greșit și rău înaintea Ta am făcut..."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayersContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(BackgroundTheme)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Rugăciuni de trebuință",
            style = MaterialTheme.typography.titleLarge,
            color = TextTitle,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(prayersList) { prayer ->
                PrayerCard(prayer)
            }
        }
    }
}

@Composable
fun PrayerCard(prayer: Prayer) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = prayer.title,
                style = MaterialTheme.typography.titleMedium,
                color = BrandRed,
                fontWeight = FontWeight.Bold
            )
            
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = BorderColor)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = prayer.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    lineHeight = 24.sp
                )
            }
        }
    }
}
