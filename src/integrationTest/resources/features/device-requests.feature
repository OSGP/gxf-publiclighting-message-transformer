Feature: Device request message processing

  Scenario: Transform get status device request from object to bytes message
    Given a device request object message of type GET_STATUS
    When the object message is sent to the inbound requests queue
    Then a device request bytes message of type GET_STATUS_REQUEST should be sent to the outbound requests queue
    And the device request bytes message should contain a valid get status request

  Scenario: Transform set light device request from object to bytes message
    Given a device request object message of type SET_LIGHT
    When the object message is sent to the inbound requests queue
    Then a device request bytes message of type SET_LIGHT_REQUEST should be sent to the outbound requests queue
    And the device request bytes message should contain a valid set light request

  Scenario: Transform set schedule device request from object to bytes message
    Given a device request object message of type SET_SCHEDULE
    When the object message is sent to the inbound requests queue
    Then a device request bytes message of type SET_SCHEDULE_REQUEST should be sent to the outbound requests queue
    And the device request bytes message should contain a valid set schedule request
