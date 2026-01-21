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
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ActionTime
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestHeader
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TransitionType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TriggerType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.Weekday
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DOMAIN
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DOMAIN_VERSION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.CORRELATION_UID
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DEFAULT_PRIORITY
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DOMAIN
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DOMAIN_VERSION
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.ORGANIZATION_IDENTIFICATION
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
        scenarioContext.inboundRequestMessage = InboundRequestMessageFactory.requestMessageForType(objectMessageType)
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

    @Then("the device request bytes message should contain a valid {requestType} request")
    fun thenBytesMessageShouldContainRequest(expectedRequestType: RequestType) {
        when (expectedRequestType) {
            RequestType.SET_CONFIGURATION_REQUEST -> verifySetConfigurationRequest()
            RequestType.RESUME_SCHEDULE_REQUEST -> verifyResumeScheduleRequest()
            RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST -> verifySetEventNotificationMaskRequest()
            RequestType.SET_LIGHT_REQUEST -> verifySetLightRequest()
            RequestType.SET_SCHEDULE_REQUEST -> verifySetScheduleRequest()
            RequestType.SET_TRANSITION_REQUEST -> verifySetTransitionRequest()
            else -> verifyEmptyRequest(expectedRequestType)
        }
    }

    private fun verifyEmptyRequest(requestType: RequestType) {
        with(scenarioContext.outboundRequestMessage) {
            assertThat(this).isNotNull.isInstanceOf(DeviceRequestMessage::class.java)
            verifyHeader(this!!.header, requestType)
            verifyNoPayload(this)
        }
    }

    private fun verifySetConfigurationRequest() {
        with(scenarioContext.outboundRequestMessage) {
            assertThat(this).isNotNull.isInstanceOf(DeviceRequestMessage::class.java)
            verifyHeader(this!!.header, RequestType.SET_CONFIGURATION_REQUEST)
            verifySetConfigurationPayload(this)
        }
    }

    private fun verifyResumeScheduleRequest() {
        with(scenarioContext.outboundRequestMessage) {
            assertThat(this).isNotNull.isInstanceOf(DeviceRequestMessage::class.java)
            verifyHeader(this!!.header, RequestType.RESUME_SCHEDULE_REQUEST)
            verifyResumeSchedulePayload(this)
        }
    }

    private fun verifySetEventNotificationMaskRequest() {
        with(scenarioContext.outboundRequestMessage) {
            assertThat(this).isNotNull.isInstanceOf(DeviceRequestMessage::class.java)
            verifyHeader(this!!.header, RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST)
            verifySetEventNotificationMaskPayload(this)
        }
    }

    private fun verifySetLightRequest() {
        with(scenarioContext.outboundRequestMessage) {
            assertThat(this).isNotNull.isInstanceOf(DeviceRequestMessage::class.java)
            verifyHeader(this!!.header, RequestType.SET_LIGHT_REQUEST)
            verifySetLightPayload(this)
        }
    }

    private fun verifySetScheduleRequest() {
        with(scenarioContext.outboundRequestMessage) {
            assertThat(this).isNotNull.isInstanceOf(DeviceRequestMessage::class.java)
            verifyHeader(this!!.header, RequestType.SET_SCHEDULE_REQUEST)
            verifySetSchedulePayload(this)
        }
    }

    private fun verifySetTransitionRequest() {
        with(scenarioContext.outboundRequestMessage) {
            assertThat(this).isNotNull.isInstanceOf(DeviceRequestMessage::class.java)
            verifyHeader(this!!.header, RequestType.SET_TRANSITION_REQUEST)
            verifySetTransitionPayload(this)
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
                message.setStringProperty(JMS_PROPERTY_DOMAIN, DOMAIN)
                message.setStringProperty(JMS_PROPERTY_DOMAIN_VERSION, DOMAIN_VERSION)
                message.setStringProperty(JMS_PROPERTY_NETWORK_ADDRESS, ipAddress)
                message.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, organisationIdentification)
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
        assertThat(message.hasSetLightRequest()).isFalse
        assertThat(message.hasSetScheduleRequest()).isFalse
    }

    private fun verifySetConfigurationPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetConfigurationRequest()).isTrue
        assertThat(message.setConfigurationRequest).isNotNull
        with(message.setConfigurationRequest.configuration) {
            assertThat(this).isNotNull
            assertThat(this.astronomicalOffsetsConfiguration).isNotNull
            assertThat(this.relayConfiguration).isNotNull
            assertThat(this.daylightSavingsTimeConfiguration).isNotNull
            assertThat(this.communicationConfiguration).isNotNull
            assertThat(this.deviceAddressConfiguration).isNotNull
            assertThat(this.platformAddressConfiguration).isNotNull
        }
    }

    private fun verifyResumeSchedulePayload(message: DeviceRequestMessage) {
        assertThat(message.hasResumeScheduleRequest()).isTrue
        with(message.resumeScheduleRequest) {
            assertThat(this).isNotNull
            assertThat(immediate).isTrue
        }
    }

    private fun verifySetEventNotificationMaskPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetEventNotificationMaskRequest()).isTrue
        with(message.setEventNotificationMaskRequest) {
            assertThat(this).isNotNull
            assertThat(this.notificationTypesList).isNotEmpty.hasSize(3)
        }
    }

    private fun verifySetLightPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetLightRequest()).isTrue
        with(message.getSetLightRequest()) {
            assertThat(this).isNotNull
            assertThat(lightValuesCount).isEqualTo(2)
            with(this.getLightValues(0)) {
                assertThat(index).isEqualTo(RelayIndex.RELAY_TWO)
                assertThat(lightOn).isTrue
            }
            with(this.getLightValues(1)) {
                assertThat(index).isEqualTo(RelayIndex.RELAY_THREE)
                assertThat(lightOn).isTrue
            }
        }
    }

    private fun verifySetSchedulePayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetScheduleRequest()).isTrue
        with(message.getSetScheduleRequest()) {
            assertThat(this).isNotNull
            assertThat(scheduleEntriesList.size).isEqualTo(4)
            with(this.scheduleEntriesList[0]) {
                assertThat(weekday).isEqualTo(Weekday.ALL_DAYS)
                assertThat(actionTime).isEqualTo(ActionTime.SUNSET_TIME)
                assertThat(triggerType).isEqualTo(TriggerType.ASTRONOMICAL)
                assertThat(valueList.size).isEqualTo(1)
                with(this.valueList[0]) {
                    assertThat(index).isEqualTo(RelayIndex.ALL_RELAYS)
                    assertThat(lightOn).isTrue
                }
            }
            with(this.scheduleEntriesList[1]) {
                assertThat(weekday).isEqualTo(Weekday.ALL_DAYS)
                assertThat(actionTime).isEqualTo(ActionTime.SUNRISE_TIME)
                assertThat(triggerType).isEqualTo(TriggerType.ASTRONOMICAL)
                assertThat(valueList.size).isEqualTo(1)
                with(this.valueList[0]) {
                    assertThat(index).isEqualTo(RelayIndex.ALL_RELAYS)
                    assertThat(lightOn).isFalse
                }
            }
            with(this.scheduleEntriesList[2]) {
                assertThat(weekday).isEqualTo(Weekday.ALL_DAYS)
                assertThat(actionTime).isEqualTo(ActionTime.ABSOLUTE_TIME)
                assertThat(time).isEqualTo("230000")
                assertThat(valueList.size).isEqualTo(1)
                with(this.valueList[0]) {
                    assertThat(index).isEqualTo(RelayIndex.RELAY_THREE)
                    assertThat(lightOn).isFalse
                }
            }
            with(this.scheduleEntriesList[3]) {
                assertThat(weekday).isEqualTo(Weekday.ALL_DAYS)
                assertThat(actionTime).isEqualTo(ActionTime.ABSOLUTE_TIME)
                assertThat(time).isEqualTo("060000")
                assertThat(valueList.size).isEqualTo(1)
                with(this.valueList[0]) {
                    assertThat(index).isEqualTo(RelayIndex.RELAY_THREE)
                    assertThat(lightOn).isTrue
                }
            }
        }
    }

    private fun verifySetTransitionPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetTransitionRequest()).isTrue
        with(message.getSetTransitionRequest()) {
            assertThat(this).isNotNull
            assertThat(transitionType).isEqualTo(TransitionType.SUNSET)
        }
    }

    private fun BytesMessage.parseIntoDeviceRequestMessage(): DeviceRequestMessage {
        val length = this.bodyLength.toInt()
        val bytes = ByteArray(length)
        this.readBytes(bytes)
        return DeviceRequestMessage.parseFrom(bytes)
    }
}
