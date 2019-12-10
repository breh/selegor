package selegor.impl

data class SelePath(val pathElements: List<SelePathElement>) : List<SelePathElement> by pathElements {
    fun asString(): String = pathElements.joinToString(separator = ".") { it.asString() }

    override fun toString(): String = "SelePath(\"${asString()}\")"

    companion object {
        fun fromString(path: String): SelePath = parseSelePath(path)
    }
}
