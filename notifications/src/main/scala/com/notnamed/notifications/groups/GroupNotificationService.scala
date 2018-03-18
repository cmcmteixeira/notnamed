package com.notnamed.notifications.groups

import Events.NewGroupEvent
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.Future

object GroupNotificationService {

}

class GroupNotificationService extends StrictLogging {
  def sendNewGroupNotification(newGroupEvent: NewGroupEvent): Future[Unit] = {
    logger.info(s"Notifing creator for group ${newGroupEvent.groupId} because he/she created an group")
    Future.successful(())
  }
}
