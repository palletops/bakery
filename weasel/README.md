# bakery-weasel

A [leaven][leaven] component for running a [weasel][weasel] based
clojurescript REPL.

## Install

Add `[com.palletops/bakery-weasel "0.3.1-SNAPSHOT"]` to your
`:dependencies` and `cemerick.piggieback/wrap-cljs-repl` to your project middleware.

```clj
:dependencies [[com.palletops/bakery-weasel "0.3.1-SNAPSHOT"]]
:repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
```
## Usage

The `com.palletops.bakery.weasel/weasel` function returns a
[leaven][leaven] component, in both clojure and clojurescript.  It
takes a single map as argument with the following keys.

`:host`
: the hostname to bind or connect to.  Defaults to "localhost".

`:port`
: the port to listen on or connect to.  The default port is 9001.

### Clojure Server

The component does not implement the `Startable` and `Stoppable`
protocols.  To start the clojurescript repl you can call the zero
argument function placed on the component's `:start-repl!` key.

### Clojurescript Client

The `Startable` and `Stoppable` implementations will start and stop
the client connection.

## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[weasel]:https://github.com/tomjakubowski/weasel "Weasel websocket based repl"
[leaven]:https://github.com/palletops/leaven "Leaven component library"
[ring]:https://github.com/ring-clojure/ring "Ring"
