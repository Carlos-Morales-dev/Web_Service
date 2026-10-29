package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Post
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Publicaciones", appName)
    }

    @Test
    fun `post model creation verifies fields`() {
        val post = Post(userId = 1, id = 42, title = "Título prueba", body = "Cuerpo prueba")
        assertEquals(1, post.userId)
        assertEquals(42, post.id)
        assertEquals("Título prueba", post.title)
        assertEquals("Cuerpo prueba", post.body)
    }
}
