package org.n9ne.common.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** One complete server timeline per account, including scheduled changes and revision. */
@Entity
data class GoalCacheEntity(@PrimaryKey val userId: Long, val snapshot: String)
