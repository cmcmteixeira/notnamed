package com.notnamed.user.database.dao
import com.notnamed.commons.database.dao.KeyedDao
import com.notnamed.user.database.entity.User
import com.notnamed.user.database.table.Users
import slick.jdbc.MySQLProfile.api.Database


class UserDao(val db: Database) extends KeyedDao[Users,User]
