// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.consumer

import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import jakarta.jms.BytesMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.lfenergy.gxf.publiclighting.contracts.internal.audittrail.LogItemMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.InboundLogItemMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.producer.LogItemMessageSender
import org.springframework.boot.test.system.OutputCaptureExtension

@ExtendWith(MockKExtension::class, OutputCaptureExtension::class)
class LogItemMessageListenerTest {
    @MockK
    lateinit var logItemMessageSender: LogItemMessageSender

    @InjectMockKs
    lateinit var logItemMessageListener: LogItemMessageListener

    @Test
    fun `should handle log item message`() {
        val logItem = InboundLogItemMessageFactory.protobufMessageForLogItem()
        val bytesMessage = setupBytesMessageMock(logItem)
        every { logItemMessageSender.send(any<LogItemMessage>()) } just Runs

        logItemMessageListener.onMessage(bytesMessage)

        verify {
            logItemMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(LogItemMessage::class.java).isEqualTo(logItem)
                },
            )
        }
    }

    private fun setupBytesMessageMock(logItemMessage: LogItemMessage): BytesMessage {
        val bytesMessage = mockk<BytesMessage>()
        val bytes = logItemMessage.toByteArray()
        every { bytesMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION) } returns
            logItemMessage.deviceIdentification
        every { bytesMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION) } returns
            logItemMessage.organizationIdentification
        every { bytesMessage.jmsType } returns logItemMessage.messageType.name
        every { bytesMessage.bodyLength } returns bytes.size.toLong()
        every { bytesMessage.readBytes(any<ByteArray>()) } answers { copyBytesToBuffer(bytes, arg(0)) }
        return bytesMessage
    }

    private fun copyBytesToBuffer(
        bytes: ByteArray,
        buffer: ByteArray,
    ): Int {
        System.arraycopy(bytes, 0, buffer, 0, bytes.size)
        return bytes.size
    }
}
