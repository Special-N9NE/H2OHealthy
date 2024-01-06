package org.n9ne.common

import android.content.Context
import android.content.res.Configuration
import org.n9ne.common.util.Utils
import java.util.Locale

object LocaleService {

    fun updateBaseContextLocale(context: Context): Context {
        val language = Utils.getLocal().language
        val locale = Locale(language)
        Locale.setDefault(locale)
        updateResourcesLocale(context, locale)
        return updateResourcesLocaleLegacy(context, locale)
    }

    private fun updateResourcesLocale(
        context: Context,
        locale: Locale
    ): Context? {
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(
        context: Context,
        locale: Locale
    ): Context {
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)
        return context
    }
}