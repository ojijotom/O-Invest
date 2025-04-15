package com.ojijo.o_invest.repository

import com.ojijo.o_invest.data.UserDao
import com.ojijo.o_invest.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: User) {
        userDao.registerUser(user)
    }

    suspend fun loginUser(email: String, password: String): User? {
        return userDao.loginUser(email, password)
    }
}