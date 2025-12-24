package com.dmoser.codyssey.bifroest.flags;

/**
 * Flag used to signal that the current shell layer should be exited.
 *
 * <p>When this flag is handled, the system will attempt to exit one shell layer. If the user is
 * already at the root layer, this flag will have no effect.
 */
public class ShellExitFlag extends AbstractFlag {}
