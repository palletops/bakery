# bakery-httpkit

A [leaven][leaven] component for running a [http-kit][http-kit] server.

## Install

Add `[com.palletops/bakery-httpkit "0.2.0-SNAPSHOT"]` to your
`:dependencies`.

## Usage

The `com.palletops.bakery.httpkit/httpkit` function returns a
[leaven][leaven] component.

It takes a single map argument with the following keys:


`:handler`
: the [ring][ring] handler.  Must be supplied.

`:stop-timeout`
: a timeout, in ms, used when waiting for the server to process outstanding requests
  when stopping.  The default is 100ms.

`:config`
: a map that will be passed to httpkit's `run-server` function.  The
  default port is 3000.


## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[http-kit]:http://http-kit.org/ "http-kit web server"
[leaven]:https://github.com/palletops/leaven "Leaven component library"
[ring]:https://github.com/ring-clojure/ring "Ring"
