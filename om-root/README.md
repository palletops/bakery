# bakery-om-root

A [leaven][leaven] component for a root [om][om] component.

## Install

Add `[com.palletops/bakery-om-root "0.2.0-SNAPSHOT"]` to your
`:dependencies`.

## Usage

The `com.palletops.bakery.om-root/om-root`
function returns a [leaven][leaven] component that is an atom backed
by html5 local storage if supported by the javascript runtime.  It
accepts two arguments with the following values:

`default`
: a value for the atom if not already in local storage.

`key`
: a keyword for the key to store the atom under in local storage.

## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[om]:https://github.com/swannodette/om "Om"
[leaven]:https://github.com/palletops/leaven "Leaven component library"
