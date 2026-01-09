// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import com.google.protobuf.ByteString
import org.joda.time.DateTime
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RelayIndex
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object CommonMappingFunctions {
    const val TIME_FORMAT = "HHmmss"
    val hhmmssFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT)

    /**
     * Converts an Int (1..4) to a ByteString containing a single byte.
     * Throws if the value is not in 1..4.
     */
    fun Int.toByteString(): ByteString {
        require(this in 1..4) { "Value must be between 1 and 4, but was $this" }
        return ByteString.copyFrom(byteArrayOf(this.toByte()))
    }

    fun Int.toRelayIndex() =
        when (this) {
            0 -> RelayIndex.ALL_RELAYS
            1 -> RelayIndex.RELAY_ONE
            2 -> RelayIndex.RELAY_TWO
            3 -> RelayIndex.RELAY_THREE
            4 -> RelayIndex.RELAY_FOUR
            else -> throw IllegalArgumentException("Unsupported relay index: $this")
        }

    fun ZonedDateTime.toUtcTimeString(): String = this.withZoneSameInstant(ZoneOffset.UTC).format(hhmmssFormatter)

    fun DateTime.toProtobuf(): String = this.toString(TIME_FORMAT)
}
