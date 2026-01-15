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
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.InboundResponseMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.producer.DeviceResponseMessageSender
import org.springframework.boot.test.system.OutputCaptureExtension

@ExtendWith(MockKExtension::class, OutputCaptureExtension::class)
class DeviceResponseMessageListenerTest {
    @MockK
    lateinit var deviceResponseMessageSender: DeviceResponseMessageSender

    @InjectMockKs
    lateinit var deviceResponseMessageListener: DeviceResponseMessageListener

    @ParameterizedTest(name = "with response type: {0}")
    @MethodSource("responseTypeProvider")
    fun `should handle response messages in protobuf bytesmessage format`(responseType: ResponseType) {
        val response = InboundResponseMessageFactory.protobufMessageForResponseOfType(responseType)
        val bytesMessage = setupBytesMessageMock(response)
        every { deviceResponseMessageSender.send(any<DeviceResponseMessage>()) } just Runs

        deviceResponseMessageListener.onMessage(bytesMessage)

        verify {
            deviceResponseMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceResponseMessage::class.java).isEqualTo(response)
                    PAYLOAD_PER_RESPONSE_TYPE_ASSERTIONS[responseType]?.invoke(it)
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

    private fun copyBytesToBuffer(
        bytes: ByteArray,
        buffer: ByteArray,
    ): Int {
        System.arraycopy(bytes, 0, buffer, 0, bytes.size)
        return bytes.size
    }

    companion object {
        private val PAYLOAD_PER_RESPONSE_TYPE_ASSERTIONS: Map<ResponseType, DeviceResponseMessage.() -> Unit> =
            mapOf(
                ResponseType.GET_FIRMWARE_VERSION_RESPONSE to { assertThat(hasGetFirmwareVersionResponse()).isTrue },
                ResponseType.GET_STATUS_RESPONSE to { assertThat(hasGetStatusResponse()).isTrue },
                ResponseType.GET_CONFIGURATION_RESPONSE to { assertThat(hasGetConfigurationResponse()).isTrue },
                ResponseType.GET_LIGHT_STATUS_RESPONSE to { assertThat(hasGetStatusResponse()).isTrue },
            )

        @JvmStatic
        private fun responseTypeProvider() =
            ResponseType.entries
                .filterNot { it == ResponseType.UNRECOGNIZED }
                .map { Arguments.of(it) }
                .stream()
    }
}
