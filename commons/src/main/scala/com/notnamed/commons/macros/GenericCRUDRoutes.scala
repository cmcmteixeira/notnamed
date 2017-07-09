package com.notnamed.commons.macros

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.http.scaladsl.marshalling.ToResponseMarshaller

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext

object GenericCRUDRoutes {
  def genericCrud[Entity]
  (table: TableQuery[_ <: Table[Entity]], db: Database):
  (FromRequestUnmarshaller[Entity],ToResponseMarshaller[Entity],ExecutionContext) => Route = macro GenericCRUDRoutes_impl.genericCrud_impl[Entity]

}

object GenericCRUDRoutes_impl {
  private case class TypeAndName(theType: String, name: String)
  def genericCrud_impl[Entity: c.WeakTypeTag]
  (c: blackbox.Context)
  (table : c.Expr[TableQuery[_ <: Table[Entity]]], db: c.Expr[Database]):
  c.Expr[(FromRequestUnmarshaller[Entity],ToResponseMarshaller[Entity],ExecutionContext) => Route] = {
    import c.universe._
    val entityType = weakTypeOf[Entity]

    val entityName = q"${entityType.toString.toLowerCase}"
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
    //no id found for copyMethodsParamNames
    val expr = c.Expr[(FromRequestUnmarshaller[_],ToResponseMarshaller[_],ExecutionContext) => Route](
      q"""
      new {
        import akka.http.scaladsl.model.StatusCodes
        import akka.http.scaladsl.server.Route
        val db = $db
        val tq = $table
        def apply(implicit unmarshaller: FromRequestUnmarshaller[$entityType], marshaller: ToResponseMarshaller[$entityType], ec: ExecutionContext) : Route = {
          path($entityName) {
            post{entity(as[$entityType])} { e : $entityType => complete {
                val query = (tq returning tq.map(_.id) into ((entity, id) => entity.copy(id = id)))
                val future = db.run(query += e)
                onComplete(future){
                  case Success(value) => complete(StatusCodes.Created, value)
                  case Failure(e) => complete(StatusCodes.Created, e)
                }
              }
            }
          }
       }
      }
      """
    )
    println(showCode(expr.tree))
    expr
  }
}


//MacrosLearning.genericCrud(DataContainer[TestCaseClassMegaAltamente](TestCaseClassMegaAltamente(2,3)))

/**

MacrosLearning.genericCrud(TestCaseClass("2",3))
    println(className)
    val fields = table.tree.tpe.widen
    val expr = q"val a=$table;println(a);println(a);println(a)"
    println(showCode(expr))


  */

/*

object GenericCRUDRoutes {
  def genericCrud[U](table: KeyedTable with Table[U]): Unit = macro genericCrud_impl[U]

  def genericCrud_impl(c: blackbox.Context)(table: c.Expr[Table[_]]): c.Expr[Unit] = {
    import c.universe._

    reify { println(s"${weakTypeOf[T].toString}") }
  }
}

*/
