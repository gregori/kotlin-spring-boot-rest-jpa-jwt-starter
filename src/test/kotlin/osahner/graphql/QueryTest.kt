package osahner.graphql

import com.graphql.spring.boot.test.GraphQLResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(
  properties = [
    "spring.autoconfigure.exclude=com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration",
    "spring.autoconfigure.exclude=com.oembedler.moon.graphql.boot.GraphQLWebsocketAutoConfiguration"
  ]
)
class GraphqlServiceApplicationTest(
  @Autowired private val restTemplate: TestRestTemplate
) {
  val loginForm = hashMapOf("username" to "john.doe", "password" to "test1234")

  // FIXME https://github.com/graphql-java-kickstart/graphql-spring-boot/issues/132

  @Test
  fun version() {
    val expected = """
{
  "data": {
    "version": "1.0.0"
  }
}
    """.trimIndent()

    val request = forJson("""{"query":"{version}","variables":null}""", HttpHeaders())
    val result = GraphQLResponse(restTemplate.exchange("/graphql", HttpMethod.POST, request, String::class))

    assertNotNull(result)
    assertEquals(HttpStatus.OK, result.statusCode)
    JSONAssert.assertEquals(expected, result.rawResponse.body, JSONCompareMode.LENIENT)
  }


  @Test
  fun `query User`() {
    val query = """
      query {
        user(id: \"john.doe\") {
          id
          username
          firstName
          lastName
          roles {
            id
            roleName
            description
          }
        }
      }
      """.replace("\n", "")

    val expected = """
      {
        "data": {
          "user": {
            "id": "2",
            "username": "john.doe",
            "firstName": "John",
            "lastName": "Doe",
            "roles": [
              {
                "id": "2",
                "roleName": "STANDARD_USER",
                "description": "Standard User - Has no admin rights"
              }
            ]
          }
        }
      }
    """.trimIndent()

    val login = restTemplate.postForEntity<String>("/login", loginForm)
    val bearer = login.headers["authorization"]?.get(0).orEmpty()
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    headers["Authorization"] = bearer

    val request = forJson("""{"query":"$query","variables":null}""", headers)
    val result = GraphQLResponse(restTemplate.exchange("/graphql", HttpMethod.POST, request, String::class))

    assertNotNull(result)
    assertEquals(HttpStatus.OK, result.statusCode)
    JSONAssert.assertEquals(expected, result.rawResponse.body, JSONCompareMode.LENIENT)
  }


  fun forJson(json: String, headers: HttpHeaders): HttpEntity<Any> {
    headers.contentType = MediaType.APPLICATION_JSON
    return HttpEntity(json, headers)
  }
}
