// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import jakarta.annotation.PostConstruct
import jakarta.jms.ObjectMessage
import org.assertj.core.api.Assertions.assertThat
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.message.transformer.DeviceEventMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageEventType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationDto
import org.opensmartgridplatform.shared.infra.jms.RequestMessage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator

class DeviceEventsSteps(
    private val scenarioContext: DeviceEventsScenarioContext,
    @param:Qualifier("deviceEventsJmsTemplate") private val jmsTemplate: JmsTemplate,
) {
    @PostConstruct
    fun setup() {
        jmsTemplate.receiveTimeout = 2000
    }

    @Given("a device event bytes message of type {eventType}")
    fun givenBytesMessage(eventType: EventType) {
        scenarioContext.inboundEventType = eventType
        scenarioContext.inboundEventMessage = DeviceEventMessageFactory.protobufMessageForEventOfType(eventType)
    }

    @When("the bytes message is sent to the inbound events queue")
    fun whenBytesMessageIsSent() {
        jmsTemplate.send(scenarioContext.inboundQueue, createBytesMessage())
    }

    @Then("a device event object message of type {objectMessageEventType} should be sent to the outbound events queue")
    fun thenObjectMessageShouldBeSent(expectedMessageType: ObjectMessageEventType) {
        val outboundMessage = jmsTemplate.receive(scenarioContext.outboundQueue)!! as ObjectMessage
        verifyObjectMessage(outboundMessage, expectedMessageType)
        scenarioContext.outboundEventMessage = outboundMessage.`object` as RequestMessage
    }

    @Then("no device event object message should be sent to the outbound events queue")
    fun thenObjectMessageShouldNotBeSent() {
        val outboundMessage = jmsTemplate.receive(scenarioContext.outboundQueue)
        assertThat(outboundMessage).isNull()
    }

    @Then("the device event object message should contain a valid device registration event")
    fun thenObjectMessageShouldContainDeviceRegistrationEvent() {
        with(scenarioContext.outboundEventMessage!!) {
            assertThat(request).isNotNull().isInstanceOf(DeviceRegistrationDataDto::class.java)
        }
    }

    @Then("the device event object message should contain a valid device registration confirmation event")
    fun thenObjectMessageShouldContainDeviceRegistrationConfirmationEvent() {
        with(scenarioContext.outboundEventMessage!!) {
            assertThat(request).isNull()
        }
    }

    @Then("the device event object message should contain a valid device notification event")
    fun thenObjectMessageShouldContainDeviceNotificationEvent() {
        with(scenarioContext.outboundEventMessage!!) {
            assertThat(request).isNotNull().isInstanceOf(EventNotificationDto::class.java)
        }
    }

    private fun createBytesMessage() =
        MessageCreator { session ->
            val msg = session.createBytesMessage()
            with(scenarioContext.inboundEventMessage!!) {
                msg.jmsType = header.eventType.name
                msg.jmsCorrelationID = header.correlationUid
                msg.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, header.deviceIdentification)
                msg.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, header.organizationIdentification)
                msg.writeBytes(toByteArray())
            }
            msg
        }

    private fun verifyObjectMessage(
        message: ObjectMessage,
        expectedMessageType: ObjectMessageEventType,
    ) {
        assertThat(message.jmsType).isEqualTo(expectedMessageType.name)
        assertThat(message.jmsCorrelationID).isEqualTo(scenarioContext.inboundEventMessage!!.header.correlationUid)
        assertThat(message.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION))
            .isEqualTo(scenarioContext.inboundEventMessage!!.header.deviceIdentification)
        assertThat(message.getStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION))
            .isEqualTo(scenarioContext.inboundEventMessage!!.header.organizationIdentification)
    }
}
