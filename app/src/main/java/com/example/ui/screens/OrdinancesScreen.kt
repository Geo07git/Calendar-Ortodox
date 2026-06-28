package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class OrdinanceCategory(val title: String, val rules: List<String>)

val ordinancesList = listOf(
    OrdinanceCategory(
        title = "Zile de post și posturi",
        rules = listOf(
            "Miercurile și vinerile de peste an, afară de cele cu dezlegare, însemnate cu harți.",
            "Ajunul Bobotezei (5 ianuarie).",
            "Tăierea Capului Sfântului Ioan Botezătorul (29 august).",
            "Înălțarea Sfintei Cruci (14 septembrie).",
            "Postul Sfintelor Paști.",
            "Postul Sfinților Apostoli Petru și Pavel.",
            "Postul Adormirii Maicii Domnului.",
            "Postul Nașterii Domnului."
        )
    ),
    OrdinanceCategory(
        title = "Zile în care nu se fac nunți",
        rules = listOf(
            "În toate zilele de post de peste an.",
            "În zilele Praznicelor împărătești și în ajunul acestora.",
            "În săptămâna lăsatului sec de carne.",
            "În Postul Sfintelor Paști.",
            "În Săptămâna Luminată.",
            "În Postul Sfinților Apostoli Petru și Pavel.",
            "În Postul Adormirii Maicii Domnului.",
            "În Postul Nașterii Domnului.",
            "În perioada de la Crăciun până la Bobotează."
        )
    ),
    OrdinanceCategory(
        title = "Sărbători bisericești naționale",
        rules = listOf(
            "Înălțarea Domnului - Ziua Eroilor.",
            "30 noiembrie - Sfântul Apostol Andrei cel Întâi chemat, Ocrotitorul României."
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdinancesContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(BackgroundTheme)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Rânduieli Bisericești",
            style = MaterialTheme.typography.titleLarge,
            color = TextTitle,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(ordinancesList) { category ->
                OrdinanceCard(category)
            }
        }
    }
}

@Composable
fun OrdinanceCard(category: OrdinanceCategory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium,
                color = AccentGold,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                category.rules.forEach { rule ->
                    Row {
                        Text("•", color = BrandRed, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            text = rule,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}
