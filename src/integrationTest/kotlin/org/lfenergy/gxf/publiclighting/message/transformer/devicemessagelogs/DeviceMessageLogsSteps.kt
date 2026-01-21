// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import jakarta.annotation.PostConstruct
import jakarta.jms.ObjectMessage
import org.assertj.core.api.Assertions.assertThat
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DECODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ENCODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_IS_INCOMING
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_PAYLOAD_MESSAGE_SERIALIZED_SIZE
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DECODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.ENCODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.IS_INCOMING
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.PAYLOAD_SIZE
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator

class DeviceMessageLogsSteps(
    private val scenarioContext: DeviceMessageLogsScenarioContext,
    @param:Qualifier("deviceMessageLogsJmsTemplate")
    private val jmsTemplate: JmsTemplate,
) {
    @PostConstruct
    fun set() {
        jmsTemplate.receiveTimeout = 2000
    }

    @Given("a device communication log message")
    fun givenBytesMessage() {
        scenarioContext.inboundLogItemMessage = InboundLogItemMessageFactory.protobufMessageForLogItem()
    }

    @When("the bytes message is sent to the inbound message logs queue")
    fun whenBytesMessageIsSent() {
        jmsTemplate.send(scenarioContext.inboundQueue, createBytesMessage())
    }

    @Then("a device communication log object message should be sent to the outbound message logs queue")
    fun thenObjectMessageShouldBeSent() {
        scenarioContext.outboundLogItemMessage = jmsTemplate.receive(scenarioContext.outboundQueue)!! as ObjectMessage
    }

    @Then("the device communication log object message should contain valid device communication log message")
    fun thenObjectMessageShouldContainLogMessage() {
        assertThat(scenarioContext.outboundLogItemMessage).isNotNull
        with(scenarioContext.outboundLogItemMessage!!) {
            assertThat(this.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)).isEqualTo(DEVICE_IDENTIFICATION)
            assertThat(this.getStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION)).isEqualTo(ORGANIZATION_IDENTIFICATION)
            assertThat(this.getStringProperty(JMS_PROPERTY_IS_INCOMING)).isEqualTo(IS_INCOMING)
            assertThat(this.getStringProperty(JMS_PROPERTY_ENCODED_MESSAGE)).isEqualTo(ENCODED_MESSAGE)
            assertThat(this.getStringProperty(JMS_PROPERTY_DECODED_MESSAGE)).isEqualTo(DECODED_MESSAGE)
            assertThat(this.getIntProperty(JMS_PROPERTY_PAYLOAD_MESSAGE_SERIALIZED_SIZE)).isEqualTo(PAYLOAD_SIZE)
        }
    }

    private fun createBytesMessage() =
        MessageCreator { session ->
            val msg = session.createBytesMessage()
            with(scenarioContext.inboundLogItemMessage!!) {
                msg.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, deviceIdentification)
                msg.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, organizationIdentification)
                msg.writeBytes(toByteArray())
            }
            msg
        }
}
