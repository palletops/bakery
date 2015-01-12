# bakery-sente

A [leaven][leaven] component for running [sente][sente] asynchronous
channel over websockets.

## Install

Add `[com.palletops/bakery-sente "0.3.1-SNAPSHOT"]` to your
`:dependencies`.

## Usage

The `com.palletops.bakery.sente/sente` function returns a
[leaven][leaven] component, in both clojure and clojurescript.  It
takes a single map as argument with the following keys.

`:path`
: the path sente will use for ajax requests.  Defaults to "/chsk".

`:handler`
: the handler function that will be called to handle each message.

`:announce-fn`
: a function that will be called with the channel socket as argument
  when the channel is started.

`:config`
: a map passed to sente's `make-channel-socket` function.

### Clojure Server

The component implements the `Startable` and `Stoppable` protocols in
an idempotent fashion.  The component has a `:routes` key that
contains ring routes for handling the ajax calls used by sente.  These
routes need to be included in your applications http handler function.

### Clojurescript Client

The `Startable` and `Stoppable` implementations will start and stop
the client connection.

## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[sente]:https://github.com/ptaoussanis/sente "Sente asynchronous channels over websockets"
[leaven]:https://github.com/palletops/leaven "Leaven component library"
[ring]:https://github.com/ring-clojure/ring "Ring"
