package zhttp

package object experiment {
  type AnyRequest              = HttpMessage.AnyRequest
  type AnyResponse[-R, +E, +A] = HttpMessage.AnyResponse[R, E, A]
}
