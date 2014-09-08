# bakery-local-storage-atom

A [leaven][leaven] component for an atom backed by HTML5 local storage via [storage-atom][storage-atom] based
clojurescript REPL.

## Install

Add `[com.palletops/bakery-local-storage-atom "0.1.1-SNAPSHOT"]` to your
`:dependencies`.

## Usage

The `com.palletops.bakery.local-storage-atom/local-storage-atom`
function returns a [leaven][leaven] component that is an atom backed
by html5 local storage if supported by the javascript runtime.  It
accepts two arguments with the following values:

`default`
: a value for the atom if not already in local storage.

`key`
: a keyword for the key to store the atom under in local storage.

The component does not implement the `Startable` and `Stoppable` protocols.

## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[storage-atom]:https://github.com/alandipert/storage-atom "Local-storage backed atom"
[leaven]:https://github.com/palletops/leaven "Leaven component library"
[ring]:https://github.com/ring-clojure/ring "Ring"
