package org.n9ne.common.util

object Messages {

    const val NO_INTERNET = "No address associated with hostname"


    val errorNetwork = if (Utils.isLocalPersian())
        "خطا در برقراری ارتباط"
    else
        "Error in Connection"
}
