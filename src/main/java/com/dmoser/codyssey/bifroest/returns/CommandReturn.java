package com.dmoser.codyssey.bifroest.returns;

public record CommandReturn(RoutingFlag routingFlag, ReturnStatus returnStatus, Object value) {}
