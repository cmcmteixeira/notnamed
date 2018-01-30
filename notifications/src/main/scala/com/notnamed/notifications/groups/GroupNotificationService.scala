package com.notnamed.notifications.groups

import com.notnamed.commons.logging.{ContextualLogger, UniqueLoggingContext}
import Events.NewGroupEvent

import scala.concurrent.Future

object GroupNotificationService {

}

class GroupNotificationService extends ContextualLogger {
  def sendNewGroupNotification(newGroupEvent: NewGroupEvent): Future[Unit] = {
    logger.info(s"Notifing ${newGroupEvent.details.createdBy} because he/she created an group wih ID: ${newGroupEvent.details.id}")(UniqueLoggingContext(newGroupEvent.meta.identifier))
    Future.successful(())
  }
}
