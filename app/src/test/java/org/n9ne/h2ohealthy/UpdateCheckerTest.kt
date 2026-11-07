package org.n9ne.h2ohealthy

import org.junit.Assert.assertEquals
import org.junit.Test

class UpdateCheckerTest {
    @Test fun versionBoundaries() {
        assertEquals(UpdateKind.REQUIRED, updateKind(9, 12, 10))
        assertEquals(UpdateKind.OPTIONAL, updateKind(10, 12, 10))
        assertEquals(UpdateKind.OPTIONAL, updateKind(11, 12, 10))
        assertEquals(UpdateKind.NONE, updateKind(12, 12, 10))
        assertEquals(UpdateKind.NONE, updateKind(13, 12, 10))
    }
}
