# mw3

MW3 is intended to be a re-implementation of MicroWorld in ClojureScript: the third reimplementation, although not nearly as dramatic as the shift from C to Clojure!

It is a very long way from finished and does not even nearly work yet. Come back in a month or two.

## What I'm trying to achieve

What I'd really like to achieve is

1. A single page application, that
2. If JavaScript is available in the browser, runs mainly or wholly client side, but
3. If JavaScript isn't available, runs wholly server side
4. With as little as possible difference between the two modes.

This may not be possible, but does account for the fact that navigation is done statically in the HTML, and then re-done in the ClojureScript.
