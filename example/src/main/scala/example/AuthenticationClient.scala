package example

import zhttp.http.Headers
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.{App, ExitCode, URIO}

object AuthenticationClient extends App {

  /**
   * This example is trying to access a protected route in AuthenticationServer
   * by first making a login request to obtain a jwt token and use it to access
   * a protected route. Run AuthenticationServer before running this example.
   */
  val url = "http://localhost:8090"
  val env = ChannelFactory.auto ++ EventLoopGroup.auto()

  val program = for {
    // Making a login request to obtain the jwt token. In this example the password should be the reverse string of username.
    token    <- Client.request(s"${url}/login/username/emanresu").flatMap(_.bodyAsString)
    // Once the jwt token is procured, adding it as a Barer token in Authorization header while accessing a protected route.
    response <- Client.request(s"${url}/user/userName/greet", headers = Headers.bearerAuthorizationHeader(token))
    body     <- response.bodyAsString
    _        <- zio.console.putStrLn(body)
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.exitCode.provideCustomLayer(env)

}
