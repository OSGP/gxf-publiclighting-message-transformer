// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.common

object ApplicationConstants {
    const val JMS_PROPERTY_DEVICE_IDENTIFICATION = "DeviceIdentification"

    // Don't change this to "OrganizationIdentification", should be equal to value in platform
    const val JMS_PROPERTY_ORGANIZATION_IDENTIFICATION = "OrganisationIdentification"
    const val JMS_PROPERTY_NETWORK_ADDRESS = "NetworkAddress"
    const val JMS_PROPERTY_DOMAIN = "Domain"
    const val JMS_PROPERTY_DOMAIN_VERSION = "DomainVersion"

    const val DEVICE_TYPE = "SSLD"
}
