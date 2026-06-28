package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { com.example.data.NotificationPreferences(context) }
    val themePrefs = remember { com.example.data.ThemePreferences.getInstance(context) }
    
    var notifyHolidays by remember { mutableStateOf(prefs.notifyHolidays) }
    var notifySaints by remember { mutableStateOf(prefs.notifySaints) }
    var notifyFasting by remember { mutableStateOf(prefs.notifyFasting) }
    
    val isDarkMode by themePrefs.isDarkModeFlow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Setări Notificări", color = TextPrimary) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Personalizează ce tip de alerte dorești să primești zilnic la ora 08:00.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, BorderColor, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column {
                    SettingToggleRow(
                        title = "Tema Întunecată",
                        subtitle = "Activează modul întunecat pentru o citire mai relaxantă",
                        checked = isDarkMode,
                        onCheckedChange = { themePrefs.isDarkModeEnabled = it }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)
                    SettingToggleRow(
                        title = "Sărbători cu cruce roșie",
                        subtitle = "Notificări pentru marile praznice și sfinți importanți",
                        checked = notifyHolidays,
                        onCheckedChange = { 
                            notifyHolidays = it 
                            prefs.notifyHolidays = it
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)
                    SettingToggleRow(
                        title = "Sfinții zilei",
                        subtitle = "Notificări zilnice cu toți sfinții pomeniți",
                        checked = notifySaints,
                        onCheckedChange = { 
                            notifySaints = it 
                            prefs.notifySaints = it
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderColor)
                    SettingToggleRow(
                        title = "Zile de post și dezlegări",
                        subtitle = "Alerte pentru post, dezlegare la pește, ulei și vin",
                        checked = notifyFasting,
                        onCheckedChange = { 
                            notifyFasting = it 
                            prefs.notifyFasting = it
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingToggleRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BrandRed,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = BorderColor
            )
        )
    }
}
