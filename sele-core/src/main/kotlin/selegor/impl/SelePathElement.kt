package selegor.impl

sealed class SelePathElement {
    abstract fun asString(): String
}

data class SelePathNameElement(val pathElement: String): SelePathElement() {
    override fun asString(): String = pathElement
}

data class SelePathIndexElement(val index: Int): SelePathElement() {
    override fun asString(): String = "$SELE_PATH_BEGIN_RANGE$index$SELE_PATH_END_RANGE"
}

data class SelePathRangeElement(val indexFrom: Int?, val indexTo: Int?): SelePathElement() {
    override fun asString(): String {
        val indexFromAsString = indexFrom?.toString() ?: ""
        val indexToAsString = indexTo?.toString() ?: ""
        return "$SELE_PATH_BEGIN_RANGE$indexFromAsString$SELE_PATH_RANGE_BETWEEN$indexToAsString$SELE_PATH_END_RANGE"
    }
}

internal fun pathName(pathElement: String) = SelePathNameElement(pathElement)
internal fun pathIndex(index: Int) = SelePathIndexElement(index)
internal fun pathRange(indexFrom: Int?, indexTo: Int?) = SelePathRangeElement(indexFrom, indexTo)
internal fun pathRangeFrom(indexFrom: Int) = SelePathRangeElement(indexFrom, null)
internal fun pathRangeTo(indexTo: Int) = SelePathRangeElement(null, indexTo)
