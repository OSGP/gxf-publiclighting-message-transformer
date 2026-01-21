Feature: Transforming device communication log messages from bytes messages to object messages

  Scenario: Transform device communication log message
    Given a device communication log message
    When the bytes message is sent to the inbound message logs queue
    Then a device communication log object message should be sent to the outbound message logs queue
    And the device communication log object message should contain valid device communication log message
