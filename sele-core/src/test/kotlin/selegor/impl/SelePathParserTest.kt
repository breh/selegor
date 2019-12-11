package selegor.impl

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import kotlin.math.exp

class SelePathParserTest {

    @Test
    fun testSimplePathParser() {
        val path = "path.to.somewhere"
        val expected = listOf(pathName("path"), pathName("to"), pathName("somewhere"))
        assertEquals(expected, parseSelePath(path))
    }

    @Test
    fun testSimplePathWithIndexParser() {
        val path = "path.with.1.index"
        val expected = listOf(pathName("path"), pathName("with"), pathIndex(1), pathName("index"))
        assertEquals(expected, parseSelePath(path))
    }

    @Test
    fun testFullRange() {
        val range = "1..10"
        val expected = Pair(1,10)
        assertEquals(expected, parseRange(range))
    }

    @Test
    fun testFromRange() {
        val range = "1.."
        val expected = Pair(1,null)
        assertEquals(expected, parseRange(range))
    }

    @Test
    fun testToRange() {
        val range = "..10"
        val expected = Pair(null, 10)
        assertEquals(expected, parseRange(range))
    }

    @Test
    fun testSimplePathWithRanges() {
        val path = "path.[1..10].[..10].[1..]"
        val expected = listOf(pathName("path"), pathRange(1, 10), pathRange(null, 10), pathRange(1, null))
        assertEquals(expected, parseSelePath(path))
    }




}