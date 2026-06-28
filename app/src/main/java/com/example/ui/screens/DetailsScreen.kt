package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FastingType
import com.example.data.OrthodoxDay
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(day: OrthodoxDay, date: LocalDate, onNavigateBack: () -> Unit) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sinaxar", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Înapoi", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundTheme,
                ),
                modifier = Modifier.border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(0.dp))
            )
        },
        containerColor = BackgroundTheme
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = date.format(dateFormatter),
                style = MaterialTheme.typography.labelMedium,
                color = BrandRed,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.Top) {
                if (day.isRedCross) {
                    Text("✝", color = BrandRed, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                } else if (day.isBlackCross) {
                    Text("✝", color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                }
                
                Text(
                    text = day.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextTitle
                )
            }

            if (day.fastingType != FastingType.NONE) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentGold.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = day.fastingType.description,
                        color = TextPrimary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            DetailCard(title = "Sinaxar", content = day.synaxarion)
            DetailCard(title = "Semnificație", content = day.meaning)
            
            // Icon Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardBackground)
                    .border(1.dp, BorderColor, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.byzantine_icon_1782654140793),
                    contentDescription = "Icoană",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun DetailCard(title: String, content: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(1.dp, BorderColor, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(
                title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = AccentGold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                content,
                style = MaterialTheme.typography.bodyLarge,
                color = TextTitle,
                lineHeight = 24.sp
            )
        }
    }
}
