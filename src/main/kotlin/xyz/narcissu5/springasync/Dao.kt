package xyz.narcissu5.springasync

import com.mysql.cj.xdevapi.SessionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T> CompletableFuture<T>.await(): T =
        suspendCoroutine { cont: Continuation<T> ->
            whenComplete { result, exception ->
                println(Thread.currentThread().name)
                if (exception == null) // the future has been completed normally
                    cont.resume(result)
                else // the future has completed with an exception
                    cont.resumeWithException(exception)
            }
        }

@Component
class UserDAO {
    val sessionFactory = SessionFactory()

    @Value("\${spring.datasource.url}")
    lateinit var dataSourceUrl: String

    suspend fun getUser(id: Int): User {
        println(Thread.currentThread().name)
        val doc = sessionFactory.getSession(dataSourceUrl)
                .getSchema("test")
                .getTable("users")
                .select("id", "name")
                .where("id = :id")
                .limit(1)
                .bind("id", id)
                .executeAsync()
                .await()
        println(Thread.currentThread().name)
        val row = doc.fetchOne()
        println(Thread.currentThread().name)
        return User(row.getInt("id"), row.getString("name"))
    }
}