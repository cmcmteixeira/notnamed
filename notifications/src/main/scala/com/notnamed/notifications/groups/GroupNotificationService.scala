package com.notnamed.notifications.groups

import Events.NewGroupEvent
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.Future

object GroupNotificationService {

}

class GroupNotificationService extends StrictLogging {
  def sendNewGroupNotification(newGroupEvent: NewGroupEvent): Future[Unit] = {
    logger.info(s"Notifing ${newGroupEvent.details.createdBy} because he/she created an group wih ID: ${newGroupEvent.details.id}")
    Future.successful(())
  }
}
