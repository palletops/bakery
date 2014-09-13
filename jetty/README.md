# bakery-jetty

A [leaven][leaven] component for running a [jetty] server.

## Install

Add `[com.palletops/bakery-jetty "0.2.0-SNAPSHOT"]` to your
`:dependencies`.

## Usage

The `com.palletops.bakery.jetty/jetty` function returns a
[leaven][leaven] component.

It takes a single map argument with the following keys:


`:handler`
: the [ring][ring] handler.  Must be supplied.

`:config`
: a map that will be passed to jetty's `run-server` function.  The
  default port is 3000.


## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[jetty]:http://www.eclipse.org/jetty/ "jetty web server"
[leaven]:https://github.com/palletops/leaven "Leaven component library"
[ring]:https://github.com/ring-clojure/ring "Ring"
