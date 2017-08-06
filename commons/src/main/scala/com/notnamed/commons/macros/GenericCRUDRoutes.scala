package com.notnamed.commons.macros

import akka.http.scaladsl.server.Route
import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

object GenericCRUDRoutes {
  def genericCrud[Entity]
  (table: TableQuery[_ <: Table[Entity]], db: Database): Route = macro GenericCRUDRoutes_impl.genericCrud_impl[Entity]
}

object GenericCRUDRoutes_impl {
  private case class TypeAndName(theType: String, name: String)
  def genericCrud_impl[Entity: c.WeakTypeTag](c: blackbox.Context)
                                             (table : c.Expr[TableQuery[_ <: Table[Entity]]], db: c.Expr[Database]): c.Expr[Route] = {
    import c.universe._
    val entityType = weakTypeOf[Entity]

    val entityName = q"${entityType.toString.toLowerCase.split("\\.").last}"
    val fields = entityType.members.filter {
      case m: TermSymbol if m.isAccessor && m.isPublic =>  true
      case m => false
    }.map(field => TypeAndName(field.typeSignature.finalResultType.toString, field.name.toString))

    val copyMethod = scala.util.Try(entityType.member(TermName("copy")).asMethod) match {
      case scala.util.Success(method) => method
      case _ => c.abort(c.enclosingPosition,"entity does not have a copy method")
    }
    val copyMethodParamNames = (copyMethod.paramLists match {
      case params :: Nil => params
      case _ => c.abort(c.enclosingPosition,"entity copy signature should not be curried")
    }).map(parameter => TypeAndName(
      theType = parameter.typeSignatureIn(entityType).toString,
      name = parameter.name.toString
    ))

    val filterPatchExpression = fields.map(property => s"entity.${property.name}=== entity").mkString(" && ")
    val fieldSelectionPatchExpression = s"(${fields.map(property => s"e.${property.name}").mkString(", ")})"
    val updateStatementPatchExpression = s"(${fields.map(property =>s"""Try(json.get("${property.name}s").map(_.convertTo[${property.theType}]).getOrElse(originalEntity.${property.name})).get""").mkString(", ")})"

    val expr = q"""
      (() => {
        import akka.http.scaladsl.model.StatusCodes
        val db = $db
        val tq = $table
        pathPrefix($entityName) {
          pathEnd {
            post{
              entity(as[$entityType]) { e : $entityType =>
                val query = (tq returning tq.map(_.id) into ((entity, id) => entity.copy(id = Some(id))))
                val future = db.run(query += e)
                onComplete(future){
                  case Success(value) => complete(StatusCodes.Created, value)
                  case Failure(e) => complete(StatusCodes.InternalServerError, e.getMessage)
                }
              }
            }
          } ~
          path(IntNumber){ id =>
            put {
              entity(as[$entityType]) { e : $entityType =>
                val query = (tq returning tq.map(_.id) into ((entity, id) => entity.copy(id = Some(id))))
                val future = db.run(query += e)
                onComplete(future){
                  case Success(value) => complete(StatusCodes.Created, value)
                  case Failure(e) => complete(StatusCodes.InternalServerError, e.getMessage)
                }
              }
            } ~
            delete {
              val query = tq.filter(_.id === id).delete
              val future = db.run(query)
              onComplete(future){
                case Success(value) => if(value == 1){
                  complete(StatusCodes.OK,"")
                } else {
                  complete(StatusCodes.NotFound,"")
                }
                case Failure(e) => complete(StatusCodes.InternalServerError, e.getMessage)
              }
            } ~
            get {
              val query = tq.filter(_.id === id).result
              val future = db.run(query)
              onComplete(future){
                case Success(value) => value
                  .headOption
                  .map(ent => complete(StatusCodes.OK,ent))
                  .getOrElse(complete(StatusCodes.NotFound,""))
                case Failure(e) => complete(StatusCodes.InternalServerError, e.getMessage)
              }
            } ~
            patch {
              entity(as[String]) { jsonStr =>
                val json = jsonStr.parseJson.asJsObject.fields
                val future = db.run(
                  tq.filter(_.id === id).result
                ).map({ results =>
                  results
                    .headOption
                    .map { originalEntity =>
                      tq.filter(entity => {
                        ${c.parse(filterPatchExpression)}
                      }).map(e => {
                        ${c.parse(fieldSelectionPatchExpression)}
                      }).update((
                        ${c.parse(updateStatementPatchExpression)}
                      ))
                    }
                }).flatMap {
                  case Some(action) => db.run(action).map(x => Some(x))
                  case _ => Future.successful(None)
                }
                onComplete(future) {
                  case Success(Some(value)) if value == 1 => complete(StatusCodes.OK,"Ok")
                  case Success(Some(value)) => complete(StatusCodes.NotModified,"Not modified")
                  case Success(None) => complete(StatusCodes.NotFound,"Not found")
                  case _ => complete(StatusCodes.InternalServerError,"")
                }
              }
            }
          }
        }
      })()
      """
    //c.abort(c.enclosingPosition,showCode(expr))
    c.Expr[Route](expr)
  }
}
