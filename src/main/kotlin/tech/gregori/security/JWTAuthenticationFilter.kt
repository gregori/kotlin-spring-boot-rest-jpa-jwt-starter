package tech.gregori.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import tech.gregori.add
import tech.gregori.config.SecurityProperties
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(
  private val authManager: AuthenticationManager,
  private val securityProperties: SecurityProperties
) : UsernamePasswordAuthenticationFilter() {

  @Throws(AuthenticationException::class)
  override fun attemptAuthentication(
    req: HttpServletRequest,
    res: HttpServletResponse?
  ): Authentication {
    return try {
      val mapper = jacksonObjectMapper()

      val creds = mapper
        .readValue<tech.gregori.domain.User>(req.inputStream)

      authManager.authenticate(
        UsernamePasswordAuthenticationToken(
          creds.username,
          creds.password,
          ArrayList()
        )
      )
    } catch (e: IOException) {
      throw AuthenticationServiceException(e.message)
    }
  }

  @Throws(IOException::class, ServletException::class)
  override fun successfulAuthentication(
    req: HttpServletRequest,
    res: HttpServletResponse,
    chain: FilterChain?,
    auth: Authentication
  ) {
    val authClaims: MutableList<String> = mutableListOf()
    auth.authorities?.let { authorities ->
      authorities.forEach { claim -> authClaims.add(claim.toString()) }
    }
    val token = Jwts.builder()
      .setSubject((auth.principal as User).username)
      .claim("auth", authClaims)
      .setExpiration(Date().add(Calendar.DAY_OF_MONTH, securityProperties.expirationTime))
      .signWith(Keys.hmacShaKeyFor(securityProperties.secret.toByteArray()), SignatureAlgorithm.HS512)
      .compact()
    res.addHeader(securityProperties.headerString, securityProperties.tokenPrefix + token)
  }
}
