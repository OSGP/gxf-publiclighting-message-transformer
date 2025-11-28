// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.config

import io.cucumber.java.ParameterType
import io.cucumber.spring.CucumberContextConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageEventType
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
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

    @ParameterType("GET_STATUS_REQUEST|SET_LIGHT_REQUEST")
    fun requestType(type: String) = RequestType.valueOf(type)

    @ParameterType("GET_STATUS_RESPONSE|SET_LIGHT_RESPONSE")
    fun responseType(type: String) = ResponseType.valueOf(type)

    @ParameterType("REGISTER_DEVICE|DEVICE_REGISTRATION_COMPLETED|EVENT_NOTIFICATION")
    fun objectMessageEventType(type: String) = ObjectMessageEventType.valueOf(type)

    @ParameterType("GET_STATUS|SET_LIGHT")
    fun objectMessageType(type: String) = ObjectMessageType.valueOf(type)
}
