package osahner.graphql

import com.graphql.spring.boot.test.GraphQLTest
import com.graphql.spring.boot.test.GraphQLTestTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@GraphQLTest
class GraphqlServiceApplicationTest(@Autowired private val graphQLTestTemplate: GraphQLTestTemplate) {
  // FIXME https://github.com/graphql-java-kickstart/graphql-spring-boot/issues/132
  /*@Test
  fun version() {
    val expected = """
{
  "data": {
    "version": "1.0.0"
  }
}
    """.trimIndent()
    val result = graphQLTestTemplate.postForResource("{ version }")

    assertNotNull(result)
    assertEquals(HttpStatus.OK, result.statusCode)
    JSONAssert.assertEquals(expected, result.rawResponse.body, JSONCompareMode.LENIENT)
  }*/
}
