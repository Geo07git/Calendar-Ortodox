package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.CalendarRepository
import com.example.data.OrthodoxDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class CalendarViewModel : ViewModel() {
    private val repository = CalendarRepository()
    
    private val _todayInfo = MutableStateFlow<OrthodoxDay?>(null)
    val todayInfo: StateFlow<OrthodoxDay?> = _todayInfo.asStateFlow()

    private val _upcomingDays = MutableStateFlow<List<Pair<LocalDate, OrthodoxDay>>>(emptyList())
    val upcomingDays: StateFlow<List<Pair<LocalDate, OrthodoxDay>>> = _upcomingDays.asStateFlow()

    init {
        loadCalendarData()
    }

    private fun loadCalendarData() {
        val today = LocalDate.now()
        _todayInfo.value = repository.getDayInfo(today)
        _upcomingDays.value = repository.getUpcomingDays(today.plusDays(1), 7)
    }

    fun getDayInfoSync(date: LocalDate): OrthodoxDay {
        return repository.getDayInfo(date)
    }

    fun search(query: String): List<Pair<LocalDate, OrthodoxDay>> {
        return repository.searchHolidays(query)
    }
}
