package selegor.impl

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.UnsupportedOperationException


internal const val SELE_PATH_SEPARATOR = '.'
internal const val SELE_PATH_BEGIN_RANGE = '['
internal const val SELE_PATH_END_RANGE = ']'
internal const val SELE_PATH_RANGE_BETWEEN = ".."

/**
 * Parses path from string representation to SelePath data structure
 */
fun parseSelePath(path: String): SelePath {
    var currentIndex = 0;
    val length = path.length
    val pathElements = mutableListOf<SelePathElement>()
    while (currentIndex < length && currentIndex >= 0) {
        val firstChar = path[currentIndex]
        val possibleRange = firstChar == SELE_PATH_BEGIN_RANGE
        val separator = if (possibleRange) SELE_PATH_END_RANGE else SELE_PATH_SEPARATOR
        val nextSeparatorIndex = path.indexOf(separator, currentIndex)
        val pathElement = if (possibleRange && nextSeparatorIndex >= 0) {
            if (nextSeparatorIndex < 0) {
                throw IllegalArgumentException("invalid path at index $currentIndex : `$path`")
            } else {
                path.substring(currentIndex, nextSeparatorIndex + 1)
            }
        } else {
            if (nextSeparatorIndex > 0) path.substring(currentIndex, nextSeparatorIndex) else path.substring(
                currentIndex
            )
        }

        if (pathElement.length == 0) throw IllegalArgumentException("invalid path at index $currentIndex : `$path`")

        val parsedElement = when {
            firstChar == SELE_PATH_BEGIN_RANGE -> {
                if (pathElement.last() == SELE_PATH_END_RANGE && pathElement.length > 4) {
                    val range = parseRange(pathElement.substring(1, pathElement.lastIndex))
                    if (range != null) {
                        pathRange(range.first, range.second)
                    } else {
                        throw IllegalArgumentException("invalid range at index $currentIndex : `$path`")
                    }
                } else {
                    throw IllegalArgumentException("invalid range at index $currentIndex : `$path`")
                }
            }
            firstChar.isDigit() -> {
                val index = Integer.parseInt(pathElement)
                pathIndex(index)
            }
            firstChar.isLetter() -> {
                pathName(pathElement)
            }
            else -> throw IllegalStateException("invalid path  at index $currentIndex : `$path`")
        }

        pathElements.add(parsedElement)
        val nextIndexDelta = if (possibleRange) 2 else 1
        currentIndex = if (nextSeparatorIndex > 0) nextSeparatorIndex + nextIndexDelta else nextSeparatorIndex
    }
    return SelePath(pathElements)
}


// parses the body of the range
internal fun parseRange(pathElement: String): Pair<Int?, Int?>? {
    val separatorIndex = pathElement.indexOf("..")
    if (separatorIndex < 0) return null
    val fromIndex = if (separatorIndex > 0) {
        Integer.parseInt(pathElement.substring(0, separatorIndex))
    } else {
        null
    }
    val toRangeBeginIndex = separatorIndex + 2;
    val toIndex = if (toRangeBeginIndex < pathElement.length) {
        Integer.parseInt(pathElement.substring(toRangeBeginIndex))
    } else {
        null
    }
    return Pair(fromIndex, toIndex)
}


