Feature: Device event message processing

  Scenario: Transform device registration event from bytes to object message
    Given a device event bytes message of type DEVICE_REGISTRATION
    When the bytes message is sent to the inbound events queue
    Then a device event object message of type REGISTER_DEVICE should be sent to the outbound events queue
    And the device event object message should contain a valid device registration event

  Scenario: Transform device registration confirmation event from bytes to object message
    Given a device event bytes message of type DEVICE_REGISTRATION_CONFIRMATION
    When the bytes message is sent to the inbound events queue
    Then a device event object message of type DEVICE_REGISTRATION_COMPLETED should be sent to the outbound events queue
    And the device event object message should contain a valid device registration confirmation event

  Scenario: Transform device notification from bytes to object message
    Given a device event bytes message of type DEVICE_NOTIFICATION
    When the bytes message is sent to the inbound events queue
    Then a device event object message of type EVENT_NOTIFICATION should be sent to the outbound events queue
    And the device event object message should contain a valid device notification event

  Scenario: Do not transform unknown bytes message
    Given a device event bytes message of type UNRECOGNIZED
    When the bytes message is sent to the inbound events queue
    Then no device event object message should be sent to the outbound events queue
