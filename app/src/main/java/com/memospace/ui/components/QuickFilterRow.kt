package com.memospace.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.memospace.R
import com.memospace.domain.model.HomeFilter

/**
 * Clean horizontal filter chips bar for the Home workspace.
 */
@Composable
fun QuickFilterRow(
    selectedFilter: HomeFilter,
    onFilterSelected: (HomeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HomeFilter.values().forEach { filter ->
            val isSelected = filter == selectedFilter
            val labelRes = when (filter) {
                HomeFilter.ALL -> R.string.filter_all
                HomeFilter.NOTES -> R.string.filter_notes
                HomeFilter.TASKS -> R.string.filter_tasks
                HomeFilter.PINNED -> R.string.filter_pinned
                HomeFilter.TODAY -> R.string.filter_today
            }

            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = { Text(text = stringResource(id = labelRes)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}
