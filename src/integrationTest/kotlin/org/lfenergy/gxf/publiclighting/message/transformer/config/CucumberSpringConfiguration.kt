// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.config

import io.cucumber.java.ParameterType
import io.cucumber.spring.CucumberContextConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageEventType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.springframework.test.annotation.DirtiesContext

@CucumberContextConfiguration()
@SpringBootTest
@ImportTestcontainers(ArtemisContainerConfiguration::class)
@DirtiesContext
class CucumberSpringConfiguration {
    @ParameterType("DEVICE_REGISTRATION|DEVICE_REGISTRATION_CONFIRMATION|DEVICE_NOTIFICATION|UNRECOGNIZED")
    fun eventType(type: String) = EventType.valueOf(type)

    @ParameterType(
        "GET_STATUS_REQUEST|SET_LIGHT_REQUEST|REBOOT_REQUEST|START_SELF_TEST_REQUEST|STOP_SELF_TEST_REQUEST|" +
            "SET_SCHEDULE_REQUEST|RESUME_SCHEDULE_REQUEST|SET_TRANSITION_REQUEST|SET_EVENT_NOTIFICATION_MASK_REQUEST|" +
            "GET_CONFIGURATION_REQUEST|SET_CONFIGURATION_REQUEST|GET_FIRMWARE_VERSION_REQUEST",
    )
    fun requestType(type: String) = RequestType.valueOf(type)

    @ParameterType(
        "GET_STATUS_RESPONSE|SET_LIGHT_RESPONSE|REBOOT_RESPONSE|START_SELF_TEST_RESPONSE|STOP_SELF_TEST_RESPONSE|" +
            "SET_SCHEDULE_RESPONSE|RESUME_SCHEDULE_RESPONSE|SET_TRANSITION_RESPONSE|SET_EVENT_NOTIFICATION_MASK_RESPONSE|" +
            "GET_CONFIGURATION_RESPONSE|SET_CONFIGURATION_RESPONSE|GET_FIRMWARE_VERSION_RESPONSE",
    )
    fun responseType(type: String) = ResponseType.valueOf(type)

    @ParameterType("REGISTER_DEVICE|DEVICE_REGISTRATION_COMPLETED|EVENT_NOTIFICATION")
    fun objectMessageEventType(type: String) = ObjectMessageEventType.valueOf(type)

    @ParameterType(
        "GET_STATUS|SET_LIGHT|SET_REBOOT|START_SELF_TEST|STOP_SELF_TEST|SET_SCHEDULE|RESUME_SCHEDULE|" +
            "SET_TRANSITION|SET_EVENT_NOTIFICATIONS|GET_CONFIGURATION|SET_CONFIGURATION|GET_FIRMWARE_VERSION",
    )
    fun objectMessageType(type: String) = ObjectMessageType.valueOf(type)
}
