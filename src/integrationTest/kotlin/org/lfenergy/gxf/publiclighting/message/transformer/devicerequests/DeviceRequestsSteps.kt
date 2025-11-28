// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import jakarta.annotation.PostConstruct
import jakarta.jms.BytesMessage
import org.assertj.core.api.Assertions.assertThat
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestHeader
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.CORRELATION_UID
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.DEFAULT_PRIORITY
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator

class DeviceRequestsSteps(
    private val scenarioContext: DeviceRequestsScenarioContext,
    @param:Qualifier("deviceRequestsJmsTemplate")
    private val jmsTemplate: JmsTemplate,
) {
    @PostConstruct
    fun setup() {
        jmsTemplate.receiveTimeout = 2000
    }

    @Given("a device request object message of type {objectMessageType}")
    fun givenObjectMessage(objectMessageType: ObjectMessageType) {
        scenarioContext.inboundRequestType = objectMessageType
        scenarioContext.inboundRequestMessage = RequestMessageFactory.requestMessageForType(objectMessageType)
    }

    @When("the object message is sent to the inbound requests queue")
    fun whenObjectMessageIsSent() {
        jmsTemplate.send(scenarioContext.inboundQueue, createObjectMessage())
    }

    @Then("a device request bytes message of type {requestType} should be sent to the outbound requests queue")
    fun thenBytesMessageShouldBeSent(expectedRequestType: RequestType) {
        val outboundMessage = jmsTemplate.receive(scenarioContext.outboundQueue) as BytesMessage
        verifyBytesMessage(outboundMessage, expectedRequestType)
        scenarioContext.outboundRequestMessage = outboundMessage.parseIntoDeviceRequestMessage()
    }

    @Then("the device request bytes message should contain a valid get status request")
    fun thenBytesMessageShouldContainGetStatusRequest() {
        with(scenarioContext.outboundRequestMessage) {
            assertThat(this).isNotNull().isInstanceOf(DeviceRequestMessage::class.java)
            verifyHeader(this!!.header, RequestType.GET_STATUS_REQUEST)
            verifyNoPayload(this)
        }
    }

    @Then("the device request bytes message should contain a valid set light request")
    fun thenBytesMessageShouldContainSetLightRequest() {
        with(scenarioContext.outboundRequestMessage) {
            assertThat(this).isNotNull().isInstanceOf(DeviceRequestMessage::class.java)
            verifyHeader(this!!.header, RequestType.SET_LIGHT_REQUEST)
            verifySetLightPayload(this)
        }
    }

    private fun createObjectMessage() =
        MessageCreator { session ->
            val message = session.createObjectMessage()
            message.jmsType = scenarioContext.inboundRequestType?.name
            with(scenarioContext.inboundRequestMessage!!) {
                message.jmsCorrelationID = correlationUid
                message.jmsPriority = DEFAULT_PRIORITY
                message.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, deviceIdentification)
                message.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, organisationIdentification)
                message.setStringProperty(JMS_PROPERTY_NETWORK_ADDRESS, ipAddress)
                if (request != null) {
                    message.`object` = request
                }
            }
            message
        }

    private fun verifyBytesMessage(
        message: BytesMessage,
        expectedRequestType: RequestType,
    ) {
        assertThat(message).isNotNull().isInstanceOf(BytesMessage::class.java)
        with(message) {
            assertThat(jmsType).isEqualTo(expectedRequestType.name)
            assertThat(jmsCorrelationID).isEqualTo(CORRELATION_UID)
            assertThat(getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)).isEqualTo(DEVICE_IDENTIFICATION)
            assertThat(getStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION)).isEqualTo(ORGANIZATION_IDENTIFICATION)
        }
    }

    private fun verifyHeader(
        header: RequestHeader,
        expectedRequestType: RequestType,
    ) {
        with(header) {
            assertThat(deviceIdentification).isEqualTo(DEVICE_IDENTIFICATION)
            assertThat(correlationUid).isEqualTo(CORRELATION_UID)
            assertThat(organizationIdentification).isEqualTo(ORGANIZATION_IDENTIFICATION)
            assertThat(requestType).isEqualTo(expectedRequestType)
            assertThat(networkAddress).isEqualTo(NETWORK_ADDRESS)
        }
    }

    private fun verifyNoPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetLightRequest()).isFalse()
        assertThat(message.hasSetScheduleRequest()).isFalse()
    }

    private fun verifySetLightPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetLightRequest()).isTrue()
        with(message.getSetLightRequest()) {
            assertThat(this).isNotNull()
            assertThat(lightValuesCount).isEqualTo(2)
            with(this.getLightValues(0)) {
                assertThat(index).isEqualTo(RelayIndex.RELAY_TWO)
                assertThat(lightOn).isTrue()
            }
            with(this.getLightValues(1)) {
                assertThat(index).isEqualTo(RelayIndex.RELAY_THREE)
                assertThat(lightOn).isTrue()
            }
        }
    }

    private fun BytesMessage.parseIntoDeviceRequestMessage(): DeviceRequestMessage {
        val length = this.bodyLength.toInt()
        val bytes = ByteArray(length)
        this.readBytes(bytes)
        return DeviceRequestMessage.parseFrom(bytes)
    }
}
