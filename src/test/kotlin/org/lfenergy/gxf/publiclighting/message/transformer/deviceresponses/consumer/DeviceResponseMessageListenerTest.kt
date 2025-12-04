// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.consumer

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
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.DeviceResponseMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.producer.DeviceResponseMessageSender
import org.springframework.boot.test.system.OutputCaptureExtension

@ExtendWith(MockKExtension::class)
@ExtendWith(OutputCaptureExtension::class)
class DeviceResponseMessageListenerTest {
    @MockK
    lateinit var deviceResponseMessageSender: DeviceResponseMessageSender

    @InjectMockKs
    lateinit var deviceResponseMessageListener: DeviceResponseMessageListener

    @Test
    fun `should handle set light device response message`() {
        testResponse(ResponseType.SET_LIGHT_RESPONSE)
    }

    @Test
    fun `should handle get status device response message`() {
        testResponse(ResponseType.GET_STATUS_RESPONSE)
    }

    @Test
    fun `should handle reboot device response message`() {
        testResponse(ResponseType.REBOOT_RESPONSE)
    }

    @Test
    fun `should handle start self test device response message`() {
        testResponse(ResponseType.START_SELF_TEST_RESPONSE)
    }

    @Test
    fun `should handle stop self test device response message`() {
        testResponse(ResponseType.STOP_SELF_TEST_RESPONSE)
    }

    private fun testResponse(responseType: ResponseType) {
        val response = DeviceResponseMessageFactory.protobufMessageForResponseOfType(responseType)
        val bytesMessage = setupBytesMessageMock(response)
        every { deviceResponseMessageSender.send(any<DeviceResponseMessage>()) } just Runs

        deviceResponseMessageListener.onMessage(bytesMessage)

        verify {
            deviceResponseMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceResponseMessage::class.java).isEqualTo(response)
                    when (responseType) {
                        ResponseType.GET_STATUS_RESPONSE -> assertThat(it.hasGetStatusResponse()).isTrue
                        else -> {} //do nothing
                    }
                },
            )
        }
    }

    private fun setupBytesMessageMock(deviceResponseMessage: DeviceResponseMessage): BytesMessage {
        val bytesMessage = mockk<BytesMessage>()
        val bytes = deviceResponseMessage.toByteArray()
        every { bytesMessage.jmsCorrelationID } returns deviceResponseMessage.header.correlationUid
        every { bytesMessage.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION) } returns
            deviceResponseMessage.header.deviceIdentification
        every { bytesMessage.jmsType } returns deviceResponseMessage.header.responseType.name
        every { bytesMessage.bodyLength } returns bytes.size.toLong()
        every { bytesMessage.readBytes(any<ByteArray>()) } answers { copyBytesToBuffer(bytes, arg(0)) }
        return bytesMessage
    }

    private fun copyBytesToBuffer(bytes: ByteArray, buffer: ByteArray): Int {
        System.arraycopy(bytes, 0, buffer, 0, bytes.size)
        return bytes.size
    }
}
