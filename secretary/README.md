# bakery-secretary

A [leaven][leaven] component for routing with [secretary][secretary].

## Install

Add `[com.palletops/bakery-secretary "0.1.1-SNAPSHOT"]` to your
`:dependencies`.

## Usage

The `com.palletops.bakery.secretary/secretary`
function returns a [leaven][leaven] component that wires up google closure's `History` to secretary.

The component implements the `ILifecycle` protocol.

## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[secretary]:https://github.com/gf3/secretary "Secretary"
[leaven]:https://github.com/palletops/leaven "Leaven component library"
