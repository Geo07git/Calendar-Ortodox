package com.example

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.data.FastingType
import com.example.data.OrthodoxDay
import com.example.ui.screens.DetailsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.*
import com.example.viewmodel.CalendarViewModel
import com.example.worker.DailyNotificationWorker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setupDailyNotification()
        
        setContent {
            MyApplicationTheme {
                CalendarAppScreen()
            }
        }
    }

    private fun setupDailyNotification() {
        val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()
            
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailyOrthodoxNotification",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun calculateInitialDelay(): Long {
        val now = java.util.Calendar.getInstance()
        val target = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 8)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
        }
        
        if (now.after(target)) {
            target.add(java.util.Calendar.DAY_OF_MONTH, 1)
        }
        return target.timeInMillis - now.timeInMillis
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CalendarAppScreen(viewModel: CalendarViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            MainTabsScreen(viewModel, 
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToDetails = { dateString -> navController.navigate("details/$dateString") }
            )
        }
        composable("settings") {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            "details/{dateString}",
            arguments = listOf(navArgument("dateString") { type = NavType.StringType })
        ) { backStackEntry ->
            val dateString = backStackEntry.arguments?.getString("dateString")
            val date = dateString?.let { LocalDate.parse(it) } ?: LocalDate.now()
            val dayInfo = viewModel.getDayInfoSync(date)
            DetailsScreen(day = dayInfo, date = date, onNavigateBack = { navController.popBackStack() })
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainTabsScreen(
    viewModel: CalendarViewModel, 
    onNavigateToSettings: () -> Unit,
    onNavigateToDetails: (String) -> Unit
) {
    var currentTab by remember { mutableStateOf("calendar") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BrandRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("†", color = Color.White, fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Calendar Ortodox", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Search, contentDescription = "Căutare", tint = TextPrimary)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "Mai multe", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundTheme,
                ),
                modifier = Modifier.border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(0.dp))
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = NavBarBackground,
                contentColor = TextPrimary,
                modifier = Modifier.border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(0.dp))
            ) {
                val selectedColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandRed,
                    selectedTextColor = BrandRed,
                    indicatorColor = NavBarBackground,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendar") },
                    label = { Text("CALENDAR", style = MaterialTheme.typography.labelSmall) },
                    selected = currentTab == "calendar",
                    onClick = { currentTab = "calendar" },
                    colors = selectedColors
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Rugăciuni") },
                    label = { Text("RUGĂCIUNI", style = MaterialTheme.typography.labelSmall) },
                    selected = currentTab == "prayers",
                    onClick = { currentTab = "prayers" },
                    colors = selectedColors
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.List, contentDescription = "Rânduieli") },
                    label = { Text("RÂNDUIELI", style = MaterialTheme.typography.labelSmall) },
                    selected = currentTab == "ordinances",
                    onClick = { currentTab = "ordinances" },
                    colors = selectedColors
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = "Setări") },
                    label = { Text("SETĂRI", style = MaterialTheme.typography.labelSmall) },
                    selected = false,
                    onClick = onNavigateToSettings,
                    colors = selectedColors
                )
            }
        },
        containerColor = BackgroundTheme,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when (currentTab) {
            "calendar" -> CalendarContent(viewModel, innerPadding, onNavigateToDetails)
            "prayers" -> com.example.ui.screens.PrayersContent(innerPadding)
            "ordinances" -> com.example.ui.screens.OrdinancesContent(innerPadding)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CalendarContent(
    viewModel: CalendarViewModel,
    innerPadding: PaddingValues,
    onNavigateToDetails: (String) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { com.example.data.NotificationPreferences(context) }
    
    val todayInfo by viewModel.todayInfo.collectAsState()
    val upcomingDays by viewModel.upcomingDays.collectAsState()

    var permissionRequested by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(prefs.notificationsEnabled) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !permissionRequested) {
        val permissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(Unit) {
            if (!permissionState.status.isGranted) {
                permissionState.launchPermissionRequest()
            }
            permissionRequested = true
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            todayInfo?.let { day ->
                TodayHeader(day)
            }
        }

        item {
            todayInfo?.let { day ->
                TodayDetailsCard(notificationsEnabled, { 
                    notificationsEnabled = it 
                    prefs.notificationsEnabled = it
                })
            }
        }
        
        item {
            Button(
                onClick = { onNavigateToDetails(LocalDate.now().toString()) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandRed)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.AccountBox, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Citește Viețile Sfinților de azi", fontSize = 16.sp)
                    }
                    Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Următoarele zile",
                style = MaterialTheme.typography.titleLarge,
                color = TextTitle,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(upcomingDays) { (date, dayInfo) ->
            UpcomingDayCard(date, dayInfo, onClick = { onNavigateToDetails(date.toString()) })
        }
    }
}

@Composable
fun TodayHeader(day: OrthodoxDay) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM")
    val today = LocalDate.now()
    
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp)) {
        Text(
            text = "Astăzi • ${today.format(dateFormatter)}",
            style = MaterialTheme.typography.labelSmall,
            color = BrandRed,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(verticalAlignment = Alignment.Top) {
            if (day.isRedCross) {
                Text("✝", color = BrandRed, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
            } else if (day.isBlackCross) {
                Text("✝", color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
            }
            
            Text(
                text = day.title,
                style = MaterialTheme.typography.headlineLarge,
                color = TextTitle
            )
        }

        if (day.fastingType != FastingType.NONE) {
            Text(
                text = day.fastingType.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun TodayDetailsCard(notificationsEnabled: Boolean, onNotificationToggle: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(CardBackground)
            .border(1.dp, BorderColor, RoundedCornerShape(32.dp))
            .padding(24.dp)
    ) {
        Column {
            InfoRow("Apostolul Zilei", "Romani 13, 11-14; 14, 1-4")
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BorderColor)
            InfoRow("Evanghelia Zilei", "Luca 1, 1-25; 57-68, 76, 80")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Notification toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(BackgroundTheme),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Notifications, contentDescription = null, tint = BrandRed)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Notificare zilnică", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text("Activează la ora 08:00", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = onNotificationToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BrandRed,
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = BorderColor
                    )
                )
            }
        }
    }
}

@Composable
fun InfoRow(title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(title.uppercase(), style = MaterialTheme.typography.labelSmall, color = AccentGold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = TextTitle)
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Info, contentDescription = null, tint = TextTitle)
        }
    }
}

@Composable
fun UpcomingDayCard(date: LocalDate, day: OrthodoxDay, onClick: () -> Unit) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = date.format(dateFormatter),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Top) {
                    if (day.isRedCross) {
                        Text("✝ ", color = BrandRed, fontWeight = FontWeight.Bold)
                    } else if (day.isBlackCross) {
                        Text("✝ ", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = day.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )
                }
            }
            if (day.fastingType != FastingType.NONE) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = day.fastingType.description,
                    tint = TextSecondary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
