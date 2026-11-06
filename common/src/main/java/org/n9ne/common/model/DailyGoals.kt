package org.n9ne.common.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import kotlin.math.roundToInt

data class GoalChange(
    val id: Long,
    @SerializedName("target_litres") val targetLitres: Int,
    @SerializedName("previous_target") val previousTarget: Int,
    @SerializedName("effective_date") val effectiveDate: String,
    val timezone: String,
    @SerializedName("is_initial") val isInitial: Int = 0
)

data class GoalState(
    @SerializedName("user_id") val userId: Long,
    val today: String,
    @SerializedName("current_target") val currentTarget: Int,
    @SerializedName("tomorrow_target") val tomorrowTarget: Int?,
    val revision: Long,
    val changes: List<GoalChange>
) {
    fun targetOn(date: LocalDate): Int = changes.asSequence()
        .filter { LocalDate.parse(it.effectiveDate) <= date }
        .maxWithOrNull(compareBy<GoalChange> { it.effectiveDate }.thenBy { it.id })
        ?.targetLitres?.coerceAtLeast(1) ?: 3

    fun scheduledOn(date: LocalDate): Int? = changes.filter { it.effectiveDate == date.toString() }
        .maxByOrNull { it.id }?.targetLitres

    companion object {
        fun legacy(userId: Long, target: Int, today: LocalDate = LocalDate.now()): GoalState {
            val initial = if (target > 0 && target != 3) listOf(
                GoalChange(0, target, 3, today.toString(), "", 1)
            ) else emptyList()
            return GoalState(userId, today.toString(), target.coerceAtLeast(1), null, 0, initial)
        }
    }
}

data class GoalResponse(val status: Boolean, val message: String?, val data: GoalState?)
data class GoalHistoryPage(
    val changes: List<GoalChange>,
    @SerializedName("next_cursor") val nextCursor: Long?
)

data class GoalHistoryResponse(val status: Boolean, val data: GoalHistoryPage?)

data class GoalUpdate(
    val target: Int,
    @SerializedName("effective_date") val effectiveDate: String,
    val timezone: String,
    val revision: Long,
)

object GoalMath {
    fun date(value: String): LocalDate? = runCatching {
        val parts = value.replace('/', '-').split('-').map(String::toInt)
        LocalDate.of(parts[0], parts[1], parts[2])
    }.getOrNull()

    fun percentage(amountMl: Double, litres: Int): Int =
        ((amountMl.coerceAtLeast(0.0) * 100.0) / (litres.coerceAtLeast(1) * 1000.0)).roundToInt()
}
