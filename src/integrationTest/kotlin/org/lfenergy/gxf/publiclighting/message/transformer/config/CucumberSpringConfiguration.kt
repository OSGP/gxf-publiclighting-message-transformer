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

@CucumberContextConfiguration()
@SpringBootTest
@ImportTestcontainers(ArtemisContainerConfiguration::class)
class CucumberSpringConfiguration {
    @ParameterType("DEVICE_REGISTRATION|DEVICE_REGISTRATION_CONFIRMATION|DEVICE_NOTIFICATION|UNRECOGNIZED")
    fun eventType(type: String) = EventType.valueOf(type)

    @ParameterType(
        "GET_CONFIGURATION_REQUEST|GET_FIRMWARE_VERSION_REQUEST|GET_LIGHT_STATUS_REQUEST|GET_STATUS_REQUEST|" +
            "REBOOT_REQUEST|RESUME_SCHEDULE_REQUEST|SET_CONFIGURATION_REQUEST|SET_EVENT_NOTIFICATION_MASK_REQUEST|" +
            "SET_LIGHT_REQUEST|SET_SCHEDULE_REQUEST|SET_TRANSITION_REQUEST|" +
            "START_SELF_TEST_REQUEST|STOP_SELF_TEST_REQUEST",
    )
    fun requestType(type: String) = RequestType.valueOf(type)

    @ParameterType(
        "GET_CONFIGURATION_RESPONSE|GET_FIRMWARE_VERSION_RESPONSE|GET_LIGHT_STATUS_RESPONSE|GET_STATUS_RESPONSE|" +
            "REBOOT_RESPONSE|RESUME_SCHEDULE_RESPONSE|SET_CONFIGURATION_RESPONSE|SET_EVENT_NOTIFICATION_MASK_RESPONSE|" +
            "SET_LIGHT_RESPONSE|SET_SCHEDULE_RESPONSE|SET_TRANSITION_RESPONSE|" +
            "START_SELF_TEST_RESPONSE|STOP_SELF_TEST_RESPONSE",
    )
    fun responseType(type: String) = ResponseType.valueOf(type)

    @ParameterType("REGISTER_DEVICE|DEVICE_REGISTRATION_COMPLETED|EVENT_NOTIFICATION")
    fun objectMessageEventType(type: String) = ObjectMessageEventType.valueOf(type)

    @ParameterType(
        "GET_CONFIGURATION|GET_FIRMWARE_VERSION|GET_LIGHT_STATUS|GET_STATUS|RESUME_SCHEDULE|" +
            "SET_CONFIGURATION|SET_EVENT_NOTIFICATIONS|SET_LIGHT|SET_LIGHT_SCHEDULE|SET_REBOOT|" +
            "SET_TRANSITION|START_SELF_TEST|STOP_SELF_TEST",
    )
    fun objectMessageType(type: String) = ObjectMessageType.valueOf(type)
}
