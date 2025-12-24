package com.dmoser.codyssey.bifroest.flags;

/**
 * Flag used to signal that the client should be stopped.
 *
 * <p>When this flag is thrown, the client session will be terminated. If the user is connected via
 * SSH, the SSH connection will be terminated. If the CLI is running directly in a terminal where it
 * was started, it will stop the server.
 */
public class SystemExitFlag extends AbstractFlag {}
