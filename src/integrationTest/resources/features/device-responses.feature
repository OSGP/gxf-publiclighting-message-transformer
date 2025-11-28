Feature: Device response message processing

  Scenario: Transform get status device response from bytes to object message
    Given a device response bytes message of type GET_STATUS_RESPONSE
    When the bytes message is sent to the inbound responses queue
    Then a device response object message of type GET_STATUS should be sent to the outbound responses queue
    And the device response object message should contain a valid get status response

  Scenario: Transform set light device response from bytes to object message
    Given a device response bytes message of type SET_LIGHT_RESPONSE
    When the bytes message is sent to the inbound responses queue
    Then a device response object message of type SET_LIGHT should be sent to the outbound responses queue
    And the device response object message should contain a valid set light response
