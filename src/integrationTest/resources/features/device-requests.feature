Feature: Transforming device request messages from object messages to bytes messages

  Scenario Outline: Transform <inboundRequestType> request message
    Given a device request object message of type <inboundRequestType>
    When the object message is sent to the inbound requests queue
    Then a device request bytes message of type <outboundRequestType> should be sent to the outbound requests queue
    And the device request bytes message should contain a valid <outboundRequestType> request
    Examples:
      | inboundRequestType      | outboundRequestType                 |
      | GET_CONFIGURATION       | GET_CONFIGURATION_REQUEST           |
      | GET_FIRMWARE_VERSION    | GET_FIRMWARE_VERSION_REQUEST        |
      | GET_LIGHT_STATUS        | GET_LIGHT_STATUS_REQUEST            |
      | GET_STATUS              | GET_STATUS_REQUEST                  |
      | RESUME_SCHEDULE         | RESUME_SCHEDULE_REQUEST             |
      | SET_CONFIGURATION       | SET_CONFIGURATION_REQUEST           |
      | SET_EVENT_NOTIFICATIONS | SET_EVENT_NOTIFICATION_MASK_REQUEST |
      | SET_LIGHT               | SET_LIGHT_REQUEST                   |
      | SET_REBOOT              | REBOOT_REQUEST                      |
      | SET_SCHEDULE            | SET_SCHEDULE_REQUEST                |
      | SET_TRANSITION          | SET_TRANSITION_REQUEST              |
      | START_SELF_TEST         | START_SELF_TEST_REQUEST             |
      | STOP_SELF_TEST          | STOP_SELF_TEST_REQUEST              |

