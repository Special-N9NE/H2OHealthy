package com.example.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.DialogGoalEditorBinding
import com.example.home.databinding.DialogGoalHistoryBinding
import com.example.home.databinding.ItemGoalHistoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.n9ne.common.model.Activity
import org.n9ne.common.model.GoalChange
import org.n9ne.common.model.GoalHistoryPage
import org.n9ne.common.model.GoalMath
import org.n9ne.common.model.GoalState
import org.n9ne.common.util.DateUtils.georgianToPersian
import org.n9ne.common.util.Utils
import java.text.NumberFormat
import java.time.LocalDate
import org.n9ne.common.R as CommonR

internal fun goalNumber(value: Number): String =
    NumberFormat.getIntegerInstance(Utils.getLocal()).format(value)

internal fun Context.goalLitres(value: Int): String =
    getString(CommonR.string.goal_litres, goalNumber(value))

private fun goalDateText(date: LocalDate): String {
    val dateText = date.toString().replace('-', '/').georgianToPersian()
    return if (Utils.isLocalPersian()) dateText.map { if (it in '0'..'9') "۰۱۲۳۴۵۶۷۸۹"[it - '0'] else it }
        .joinToString("") else dateText
}

private fun jalaliMonthStart(date: LocalDate): LocalDate {
    val day = date.toString().replace('-', '/').georgianToPersian().substringAfterLast('/').toLong()
    return date.minusDays(day - 1)
}

private fun Context.dp(value: Int) = (value * resources.displayMetrics.density).toInt()
private fun TextView.useGoalFont() {
    typeface = ResourcesCompat.getFont(context, CommonR.font.regular)
}

private fun AlertDialog.useGoalButtonFonts() {
    getButton(AlertDialog.BUTTON_POSITIVE)?.useGoalFont()
    getButton(AlertDialog.BUTTON_NEGATIVE)?.useGoalFont()
    findViewById<TextView>(androidx.appcompat.R.id.alertTitle)?.typeface =
        ResourcesCompat.getFont(context, CommonR.font.bold)
}

internal class GoalEditor(
    private val context: Context,
    state: GoalState,
    tomorrowInitially: Boolean,
    private val onSave: (Int, Boolean) -> Unit,
) {
    private val binding = DialogGoalEditorBinding.inflate(LayoutInflater.from(context))
    private val picker = binding.picker.apply {
        adapter = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            (1..10).map { context.goalLitres(it) }
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return super.getView(position, convertView, parent).also {
                    (it as TextView).useGoalFont()
                }
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also {
                    (it as TextView).useGoalFont()
                }
            }
        }.apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val target = if (tomorrowInitially) {
            state.scheduledOn(LocalDate.now().plusDays(1)) ?: state.targetOn(
                LocalDate.now()
            )
        } else {
            state.targetOn(LocalDate.now())
        }
        setSelection(target.coerceIn(1, 10) - 1)
    }
    private val choices = binding.choices
    private val todayChoice = binding.today
    private val tomorrowChoice = binding.tomorrow
    private val error = binding.error
    private var editable = false
    private var saving = false
    private val summary = binding.summary

    var dialog: AlertDialog
        private set

    init {
        choices.check(if (tomorrowInitially) tomorrowChoice.id else todayChoice.id)
        dialog = MaterialAlertDialogBuilder(context)
            .setTitle(CommonR.string.goal_edit).setView(binding.root)
            .setNegativeButton(CommonR.string.goal_cancel, null)
            .setPositiveButton(CommonR.string.goal_save, null).create()
        dialog.setOnShowListener {
            dialog.useGoalButtonFonts()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                error.visibility = View.GONE
                onSave(
                    picker.selectedItemPosition + 1,
                    choices.checkedRadioButtonId == tomorrowChoice.id
                )
            }
            renderButtons()
        }
        update(state, false)
    }

    fun show() = dialog.show()
    fun update(state: GoalState, canEdit: Boolean) {
        editable = canEdit
        summary.text = state.scheduledOn(LocalDate.now().plusDays(1))?.let {
            context.getString(CommonR.string.goal_scheduled, context.goalLitres(it))
        }.orEmpty()
        summary.visibility = if (summary.text.isEmpty()) View.GONE else View.VISIBLE
        if (!canEdit) showError(context.getString(CommonR.string.goal_unavailable))
        renderButtons()
    }

    fun setSaving(value: Boolean) {
        saving = value; renderButtons()
    }

    fun showError(message: String) {
        error.text = message; error.visibility = View.VISIBLE
    }

    private fun renderButtons() {
        picker.isEnabled = !saving
        todayChoice.isEnabled = !saving
        tomorrowChoice.isEnabled = !saving
        dialog.setCancelable(!saving)
        if (dialog.isShowing) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = editable && !saving
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setText(if (saving) CommonR.string.goal_saving else CommonR.string.goal_save)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isEnabled = !saving
        }
    }
}

internal class GoalHistoryDialog(
    private val context: Context,
    initialState: GoalState,
    initialActivities: List<Activity>,
    private val earliest: LocalDate,
    cached: Boolean,
    private val activitiesHistory: Boolean,
    private val loadMonth: (LocalDate, LocalDate, (List<Activity>?) -> Unit) -> Unit,
    private val loadGoalPage: (Long?, (GoalHistoryPage?) -> Unit) -> Unit
) {
    private var state = initialState
    private var activities = initialActivities
    private var month = jalaliMonthStart(LocalDate.now())
    private var loading = false
    private var loadFailed = false
    private var requestSequence = 0
    private val monthCache = mutableMapOf<LocalDate, List<Activity>>()
    private var dismissed = false
    private val goalChanges = mutableListOf<GoalChange>()
    private var goalCursor: Long? = null
    private var goalHasMore = true
    private var goalLoading = false
    private var goalFailed = false
    private val binding = DialogGoalHistoryBinding.inflate(LayoutInflater.from(context))
    private val title = binding.monthTitle
    private val previous = binding.previous
    private val next = binding.next
    private val monthControls = binding.monthControls
    private val adapter = GoalRowsAdapter(context) { position ->
        if (!activitiesHistory && goalFailed && position == goalChanges.size) requestGoals()
    }
    private val dialog: AlertDialog

    init {
        binding.cached.visibility = if (cached) View.VISIBLE else View.GONE
        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GoalHistoryDialog.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!activitiesHistory && dy > 0 && !recyclerView.canScrollVertically(1) && goalHasMore && !goalLoading && !goalFailed) {
                        recyclerView.post { if (!dismissed) requestGoals() }
                    }
                }
            })
        }
        val contentHeight =
            minOf(context.dp(500), (context.resources.displayMetrics.heightPixels * 0.6f).toInt())
        binding.root.minimumHeight = contentHeight
        dialog = MaterialAlertDialogBuilder(context)
            .setTitle(
                if (activitiesHistory)
                    CommonR.string.activity_history
                else
                    CommonR.string.goals_history
            )
            .setView(binding.root)
            .setPositiveButton(CommonR.string.goal_close, null)
            .create()
        dialog.setOnShowListener {
            dialog.useGoalButtonFonts()
        }
        dialog.setOnDismissListener {
            dismissed = true
            requestSequence++
            monthCache.clear()
        }
        previous.setOnClickListener {
            month = jalaliMonthStart(month.minusDays(1))
            requestMonth()
        }
        next.setOnClickListener {
            month = jalaliMonthStart(month.plusDays(32))
            requestMonth()
        }
        render()
        if (activitiesHistory) requestMonth()
        else requestGoals()
    }

    fun show() = dialog.show()
    fun dismiss() = dialog.dismiss()
    fun update(value: GoalState, water: List<Activity>) {
        state = value
        if (!activitiesHistory) activities = water
        render()
    }

    private fun requestMonth() {
        val requested = month
        val sequence = ++requestSequence
        val cachedActivities = monthCache[requested]
        if (cachedActivities != null) {
            activities = cachedActivities
            loading = false
            loadFailed = false
            render()
            return
        }
        loading = true
        loadFailed = false
        render()
        loadMonth(requested, jalaliMonthStart(requested.plusDays(32))) { result ->
            if (dismissed) return@loadMonth
            if (result != null) monthCache[requested] = result
            if (sequence == requestSequence) {
                loading = false
                loadFailed = result == null
                activities = result.orEmpty()
                render()
            }
        }
    }

    private fun requestGoals() {
        if (goalLoading || !goalHasMore || dismissed) return
        goalLoading = true
        goalFailed = false
        render()
        loadGoalPage(goalCursor) { page ->
            if (dismissed) return@loadGoalPage
            goalLoading = false
            goalFailed = page == null
            if (page != null) {
                goalChanges.addAll(page.changes)
                goalCursor = page.nextCursor
                goalHasMore = page.nextCursor != null
            }
            render()
        }
    }

    private fun render() {
        monthControls.visibility = if (activitiesHistory) View.VISIBLE else View.GONE
        val initialLoading =
            if (activitiesHistory) loading else goalLoading && goalChanges.isEmpty()
        binding.loadingState.visibility = if (initialLoading) View.VISIBLE else View.GONE
        binding.list.visibility = if (initialLoading) View.INVISIBLE else View.VISIBLE
        binding.pageLoading.visibility =
            if (!activitiesHistory && goalLoading && goalChanges.isNotEmpty()) View.VISIBLE else View.GONE
        val today = LocalDate.now()
        val nextMonth = jalaliMonthStart(month.plusDays(32))
        previous.isEnabled = month > jalaliMonthStart(earliest)
        next.isEnabled = month < jalaliMonthStart(today)
        previous.alpha = if (previous.isEnabled) 1f else 0.38f
        next.alpha = if (next.isEnabled) 1f else 0.38f
        title.text = "${goalDateText(month)} – ${goalDateText(nextMonth.minusDays(1))}"
        val rows = if (activitiesHistory && (loading || loadFailed)) {
            listOf(context.getString(if (loading) CommonR.string.history_loading else CommonR.string.history_load_failed))
        } else if (!activitiesHistory) {
            val latest = state.changes.groupBy { it.effectiveDate }
                .mapValues { it.value.maxOf { row -> row.id } }
            val rendered = goalChanges.map { change ->
                val date = LocalDate.parse(change.effectiveDate)
                val status = when {
                    change.isInitial == 1 -> CommonR.string.goal_initial
                    latest[change.effectiveDate] != change.id -> CommonR.string.goal_superseded
                    date > today -> CommonR.string.goal_pending
                    else -> CommonR.string.goal_applied
                }
                context.getString(
                    CommonR.string.goal_change_row,
                    context.goalLitres(change.previousTarget),
                    context.goalLitres(change.targetLitres),
                    goalDateText(date)
                ) + "\n" + context.getString(status)
            }
            when {
                goalFailed -> rendered + context.getString(CommonR.string.history_load_failed)
                rendered.isEmpty() && !goalLoading -> listOf(context.getString(CommonR.string.goal_no_changes))
                else -> rendered
            }
        } else {
            val totals = activities.groupBy { GoalMath.date(it.date) }.mapValues { (_, water) ->
                water.sumOf {
                    (it.amount.toDoubleOrNull() ?: 0.0) * 1000.0
                }
            }
            (0 until java.time.temporal.ChronoUnit.DAYS.between(month, nextMonth).toInt())
                .map { month.plusDays(it.toLong()) }.filter { it <= today }
                .reversed().map { date ->
                    val amount = totals[date] ?: 0.0
                    goalDateText(date) + "\n" + context.getString(
                        CommonR.string.goal_daily_row,
                        context.goalLitres(state.targetOn(date)),
                        goalNumber(amount),
                        goalNumber(GoalMath.percentage(amount, state.targetOn(date)))
                    )
                }
        }
        adapter.rows = rows
        adapter.notifyDataSetChanged()
    }
}

private class GoalRowsAdapter(private val context: Context, private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<GoalRowsAdapter.Holder>() {
    var rows: List<String> = emptyList()

    class Holder(val binding: ItemGoalHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemGoalHistoryBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount() = rows.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.text.text = rows[position]
        holder.binding.separator.visibility =
            if (position < rows.lastIndex) View.VISIBLE else View.GONE
        holder.binding.root.setOnClickListener {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION) onClick(
                holder.bindingAdapterPosition
            )
        }
    }
}
