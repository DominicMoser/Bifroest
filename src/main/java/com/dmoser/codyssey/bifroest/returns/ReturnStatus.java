package com.dmoser.codyssey.bifroest.returns;

public sealed interface ReturnStatus permits ReturnStatus.Success, ReturnStatus.Failure {

  static final Success SUCCESS = new Success();
  static final Failure FAILURE = new Failure();

  non-sealed class Success implements ReturnStatus {}

  non-sealed class Failure implements ReturnStatus {}
}
