package org.n9ne.common.model

import java.util.Locale

enum class ActivityType(val text: String) {
    NEVER("Never"),
    LOW("Low"),
    SOMETIMES("Sometimes"),
    HIGH("High"),
    ATHLETE("Athlete");

    companion object {
        val translations = mapOf(
            Locale.ENGLISH to mapOf(
                NEVER to "Never",
                LOW to "Low",
                SOMETIMES to "Sometimes",
                HIGH to "High",
                ATHLETE to "Athlete"
            ),
            Locale("fa", "IR") to mapOf(
                NEVER to "هیچوقت",
                LOW to "کم",
                SOMETIMES to "گاهی",
                HIGH to "زیاد",
                ATHLETE to "ورزشکار"
            )
        )

        fun getLocalizedText(activityType: ActivityType, locale: Locale): String {
            return translations[locale]?.get(activityType) ?: activityType.text
        }
    }
}
