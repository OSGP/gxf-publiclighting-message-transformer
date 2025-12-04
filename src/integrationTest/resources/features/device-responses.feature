Feature: Device response message processing

  Scenario Outline: Transform empty device response from bytes to object message
    Given a device response bytes message of type <inboundResponseType>
    When the bytes message is sent to the inbound responses queue
    Then a device response object message of type <outboundResponseType> should be sent to the outbound responses queue
    And the device response object message should contain a valid empty response for type <outboundResponseType>
Examples:
    | inboundResponseType            | outboundResponseType |
    | SET_LIGHT_RESPONSE             | SET_LIGHT            |
    | REBOOT_RESPONSE                | SET_REBOOT           |
    | START_SELF_TEST_RESPONSE       | START_SELF_TEST      |
    | STOP_SELF_TEST_RESPONSE        | STOP_SELF_TEST       |
    | SET_SCHEDULE_RESPONSE          | SET_SCHEDULE         |

  Scenario: Transform get status device response from bytes to object message
    Given a device response bytes message of type GET_STATUS_RESPONSE
    When the bytes message is sent to the inbound responses queue
    Then a device response object message of type GET_STATUS should be sent to the outbound responses queue
    And the device response object message should contain a valid get status response
