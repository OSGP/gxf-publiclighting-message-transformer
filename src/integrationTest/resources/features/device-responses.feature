Feature: Device response message processing

  Scenario Outline: Transform device response from bytes to object message
    Given a device response bytes message of type <inboundResponseType>
    When the bytes message is sent to the inbound responses queue
    Then a device response object message of type <outboundResponseType> should be sent to the outbound responses queue
    And the device response object message should contain a <outboundResponseType> response
    Examples:
      | inboundResponseType                  | outboundResponseType        |
      | GET_STATUS_RESPONSE                  | GET_STATUS                  |
      | REBOOT_RESPONSE                      | SET_REBOOT                  |
      | RESUME_SCHEDULE_RESPONSE             | RESUME_SCHEDULE             |
      | SET_LIGHT_RESPONSE                   | SET_LIGHT                   |
      | SET_SCHEDULE_RESPONSE                | SET_SCHEDULE                |
      | SET_TRANSITION_RESPONSE              | SET_TRANSITION              |
      | START_SELF_TEST_RESPONSE             | START_SELF_TEST             |
      | STOP_SELF_TEST_RESPONSE              | STOP_SELF_TEST              |
#      | SET_EVENT_NOTIFICATION_MASK_RESPONSE | SET_EVENT_NOTIFICATION_MASK |
#      | GET_CONFIGURATION_RESPONSE           | GET_CONFIGURATION           |
#      | SET_CONFIGURATION_RESPONSE           | SET_CONFIGURATION           |
#      | GET_FIRMWARE_VERSION_RESPONSE        | GET_FIRMWARE_VERSION        |
