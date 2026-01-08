// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.producer

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import jakarta.jms.BytesMessage
import jakarta.jms.Session
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.OutboundRequestMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.config.DeviceRequestsConfigurationProperties
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator

@ExtendWith(MockKExtension::class)
class DeviceRequestMessageSenderTest {
    @MockK
    lateinit var jmsTemplate: JmsTemplate

    @MockK
    lateinit var properties: DeviceRequestsConfigurationProperties

    @InjectMockKs
    lateinit var deviceRequestMessageSender: DeviceRequestMessageSender

    private lateinit var bytesMessage: BytesMessage
    private lateinit var session: Session

    @BeforeEach
    fun setupMocks() {
        bytesMessage = mockk(relaxed = true)
        session = mockk(relaxed = true)

        every { properties.producer.outboundQueue } returns OUTBOUND_QUEUE_NAME
        every { session.createBytesMessage() } returns bytesMessage
        every { jmsTemplate.send(any<String>(), any<MessageCreator>()) } just runs
    }

    @ParameterizedTest(name = "with request type: {0}")
    @MethodSource("requestTypeProvider")
    fun `should send protobuf bytes message`(requestType: RequestType) {
        val message = OutboundRequestMessageFactory.deviceRequestMessage(requestType)

        deviceRequestMessageSender.send(message)

        verifyBytesMessageIsSent()
        verifyBytesMessageContainsProtobufMessage(requestType)
    }

    private fun verifyBytesMessageIsSent() {
        var creator: MessageCreator? = null
        verify { jmsTemplate.send(OUTBOUND_QUEUE_NAME, withArg { creator = it }) }
        val message = creator!!.createMessage(session)
        assertThat(message).isInstanceOf(BytesMessage::class.java).isEqualTo(bytesMessage)
    }

    private fun verifyBytesMessageContainsProtobufMessage(requestType: RequestType) {
        verify {
            bytesMessage.writeBytes(
                withArg { bytes ->
                    val deviceRequestMessage = DeviceRequestMessage.parseFrom(bytes)
                    assertThat(deviceRequestMessage.header.deviceIdentification).isEqualTo(DEVICE_IDENTIFICATION)
                    assertThat(deviceRequestMessage.header.requestType).isEqualTo(requestType)
                    PAYLOAD_PER_REQUEST_TYPE_ASSERTIONS[requestType]?.invoke(deviceRequestMessage)
                },
            )
        }
    }

    companion object {
        private const val OUTBOUND_QUEUE_NAME = "queue"

        private val PAYLOAD_PER_REQUEST_TYPE_ASSERTIONS: Map<RequestType, DeviceRequestMessage.() -> Unit> =
            mapOf(
                RequestType.RESUME_SCHEDULE_REQUEST to { assertThat(hasResumeScheduleRequest()).isTrue },
                RequestType.SET_CONFIGURATION_REQUEST to { assertThat(hasSetConfigurationRequest()).isTrue },
                RequestType.SET_LIGHT_REQUEST to { assertThat(hasSetLightRequest()).isTrue },
                RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST to { assertThat(hasSetEventNotificationMaskRequest()).isTrue },
                RequestType.SET_SCHEDULE_REQUEST to { assertThat(hasSetScheduleRequest()).isTrue },
                RequestType.SET_TRANSITION_REQUEST to { assertThat(hasSetTransitionRequest()).isTrue },
                // For other types, no payload assertion is needed
            )

        @JvmStatic
        fun requestTypeProvider() =
            RequestType.entries
                .filterNot { it == RequestType.UNRECOGNIZED }
                .map { Arguments.of(it) }
                .stream()
    }
}
