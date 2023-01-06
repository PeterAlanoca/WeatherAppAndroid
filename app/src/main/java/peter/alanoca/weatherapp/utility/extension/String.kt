package peter.alanoca.weatherapp.utility.extension

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { w -> w.replaceFirstChar { c ->  c.uppercaseChar() } }
