package com.fitness.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

enum class TimeRange {
    D, W, M, SixM, Y
}

data class StepData(
    val label: String,
    val value: Float // Normalized 0f..1f for the graph
)

@HiltViewModel
class StepViewModel @Inject constructor() : ViewModel() {

    private val _selectedRange = MutableStateFlow(TimeRange.D)
    val selectedRange: StateFlow<TimeRange> = _selectedRange.asStateFlow()

    private val _stepData = MutableStateFlow<List<StepData>>(emptyList())
    val stepData: StateFlow<List<StepData>> = _stepData.asStateFlow()

    private val _totalSteps = MutableStateFlow(0)
    val totalSteps: StateFlow<Int> = _totalSteps.asStateFlow()

    init {
        updateData(TimeRange.D)
    }

    fun setRange(range: TimeRange) {
        _selectedRange.value = range
        updateData(range)
    }

    private fun updateData(range: TimeRange) {
        viewModelScope.launch {
            // Mock data generation based on range
            val (data, total) = when (range) {
                TimeRange.D -> generateDailyData()
                TimeRange.W -> generateWeeklyData()
                TimeRange.M -> generateMonthlyData()
                TimeRange.SixM -> generateSixMonthData()
                TimeRange.Y -> generateYearlyData()
            }
            _stepData.value = data
            _totalSteps.value = total
        }
    }

    private fun generateDailyData(): Pair<List<StepData>, Int> {
        val data = listOf(
            0.05f, 0.08f, 0.1f, 0.15f, 0.2f, 0.35f, 0.6f, 0.8f, 1f, 0.75f,
            0.5f, 0.35f, 0.2f, 0.15f, 0.1f, 0.08f, 0.05f, 0.02f
        ).map { StepData("", it) }
        return data to 1352
    }

    private fun generateWeeklyData(): Pair<List<StepData>, Int> {
        val data = listOf(0.4f, 0.6f, 0.8f, 0.5f, 0.9f, 0.7f, 0.3f).map { StepData("", it) }
        return data to 25430
    }

    private fun generateMonthlyData(): Pair<List<StepData>, Int> {
        val data = List(30) { (4..10).random() / 10f }.map { StepData("", it) }
        return data to 120500
    }

    private fun generateSixMonthData(): Pair<List<StepData>, Int> {
        val data = List(6) { (5..10).random() / 10f }.map { StepData("", it) }
        return data to 850000
    }

    private fun generateYearlyData(): Pair<List<StepData>, Int> {
        val data = List(12) { (6..10).random() / 10f }.map { StepData("", it) }
        return data to 1800000
    }
}
