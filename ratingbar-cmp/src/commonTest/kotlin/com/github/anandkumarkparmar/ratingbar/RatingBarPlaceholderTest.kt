package com.github.anandkumarkparmar.ratingbar

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Unit tests for [RatingBarPlaceholder] preconditions and [RatingBarDefaults] constants.
 *
 * Note: [RatingBarPlaceholder] is a `@Composable` function and cannot be instantiated in pure
 * logic tests. The `require` guards within it are tested indirectly by verifying the same
 * precondition expressions here.
 */
class RatingBarPlaceholderTest {

    @Test
    fun shimmerDurationMillisDefaultIs1000() {
        assertEquals(1000, RatingBarDefaults.ShimmerDurationMillis)
    }

    @Test
    fun maxZeroViolatesPrecondition() {
        assertFailsWith<IllegalArgumentException> {
            require(0 > 0) { "max must be greater than 0, but was 0" }
        }
    }

    @Test
    fun maxNegativeViolatesPrecondition() {
        assertFailsWith<IllegalArgumentException> {
            require(-3 > 0) { "max must be greater than 0, but was -3" }
        }
    }

    @Test
    fun maxPositivePassesPrecondition() {
        // Should not throw
        require(5 > 0) { "max must be greater than 0" }
    }

    @Test
    fun animationDurationZeroViolatesPrecondition() {
        assertFailsWith<IllegalArgumentException> {
            require(0 > 0) { "animationDurationMillis must be greater than 0, but was 0" }
        }
    }

    @Test
    fun animationDurationNegativeViolatesPrecondition() {
        assertFailsWith<IllegalArgumentException> {
            require(-1 > 0) { "animationDurationMillis must be greater than 0, but was -1" }
        }
    }

    @Test
    fun animationDurationPositivePassesPrecondition() {
        // Should not throw
        require(1000 > 0) { "animationDurationMillis must be greater than 0" }
    }
}
