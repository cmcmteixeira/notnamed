package com.notnamed.notifications.groups

import com.notnamed.commons.kafka.{KafkaTopicConsumer, TypedKafkaEventDecoder}
import com.notnamed.notifications.Config
import com.notnamed.notifications.groups.Events.NewGroupEvent

class GroupConsumer(kafkaTopicConsumer: KafkaTopicConsumer, groupService: GroupNotificationService) {
  def consume = kafkaTopicConsumer
    .startConsumer(TypedKafkaEventDecoder(
      Config.groupEvents.newGroupEvent -> Events.Decoders.newGroupEvent
    )) {
      case event: NewGroupEvent => groupService.sendNewGroupNotification(event)
    }
}
