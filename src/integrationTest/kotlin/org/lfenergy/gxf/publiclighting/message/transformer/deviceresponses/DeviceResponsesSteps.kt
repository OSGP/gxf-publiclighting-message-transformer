// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import jakarta.annotation.PostConstruct
import jakarta.jms.ObjectMessage
import org.assertj.core.api.Assertions.assertThat
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator

class DeviceResponsesSteps(
    private val scenarioContext: DeviceResponsesScenarioContext,
    @param:Qualifier("deviceResponsesJmsTemplate")
    private val jmsTemplate: JmsTemplate,
) {
    @PostConstruct
    fun setup() {
        jmsTemplate.receiveTimeout = 2000
    }

    @Given("a device response bytes message of type {responseType}")
    fun givenBytesMessage(responseType: ResponseType) {
        scenarioContext.inboundResponseType = responseType
        scenarioContext.inboundResponseMessage = DeviceResponseMessageFactory.protobufMessageForResponseOfType(responseType)
    }

    @When("the bytes message is sent to the inbound responses queue")
    fun whenBytesMessageIsSent() {
        jmsTemplate.send(scenarioContext.inboundQueue, createBytesMessage())
    }

    @Then("a device response object message of type {objectMessageType} should be sent to the outbound responses queue")
    fun thenObjectMessageShouldBeSent(expectedMessageType: ObjectMessageType) {
        val outboundMessage = jmsTemplate.receive(scenarioContext.outboundQueue)!! as ObjectMessage
        verifyObjectMessage(outboundMessage, expectedMessageType)
        scenarioContext.outboundResponseMessage = outboundMessage.`object` as ProtocolResponseMessage
    }

    @Then("the device response object message should contain a valid get status response")
    fun thenObjectMessageShouldContainGetStatusResponse() {
        assertThat(scenarioContext.outboundResponseMessage)
            .isNotNull()
            .isInstanceOf(ProtocolResponseMessage::class.java)
        with(scenarioContext.outboundResponseMessage!!) {
            assertThat(this.messageType).isEqualTo(ObjectMessageType.GET_STATUS.name)
            assertThat(this.result).isNotNull().isEqualTo(ResponseMessageResultType.OK)
            assertThat(this.dataObject).isNotNull()
        }
    }

    @Then("the device response object message should contain a valid empty response for type {objectMessageType}")
    fun thenObjectMessageShouldContainEmptyResponse(objectMessageType: ObjectMessageType) {
        assertThat(scenarioContext.outboundResponseMessage)
            .isNotNull()
            .isInstanceOf(ProtocolResponseMessage::class.java)
        with(scenarioContext.outboundResponseMessage!!) {
            assertThat(this.messageType).isEqualTo(objectMessageType.name)
            assertThat(this.result).isNotNull().isEqualTo(ResponseMessageResultType.OK)
            assertThat(this.dataObject).isNull()
        }
    }

    private fun createBytesMessage() =
        MessageCreator { session ->
            val msg = session.createBytesMessage()
            with(scenarioContext.inboundResponseMessage!!) {
                msg.jmsType = header.responseType.name
                msg.jmsCorrelationID = header.correlationUid
                msg.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, header.deviceIdentification)
                msg.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, header.organizationIdentification)
                msg.writeBytes(toByteArray())
            }
            msg
        }

    private fun verifyObjectMessage(
        message: ObjectMessage,
        expectedMessageType: ObjectMessageType,
    ) {
        assertThat(message.jmsType).isEqualTo(expectedMessageType.name)
        assertThat(message.jmsCorrelationID).isEqualTo(scenarioContext.inboundResponseMessage!!.header.correlationUid)
        assertThat(message.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION))
            .isEqualTo(scenarioContext.inboundResponseMessage!!.header.deviceIdentification)
        assertThat(message.getStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION))
            .isEqualTo(scenarioContext.inboundResponseMessage!!.header.organizationIdentification)
    }
}
