# bakery

A library of components for [leaven][leaven].

## Install

Add `[com.palletops/bakery "0.1.0-SNAPSHOT"]` to your `:dependencies`.

Note that bakery is a heterogeneous collection.  In order not to force
a bundle of unwanted dependencies on your project, you'll need to
ensure that you add the required dependencies for each component
individually.

## Usage

Component | Description
----------|-------------
[`jetty`](src/clj/com/palletops/bakery/jetty.clj)           | a jetty server
[`httpkit`](src/clj/com/palletops/bakery/httpkit.clj)       | a httpkit server
[`local-storage-atom`](src/cljs/com/palletops/bakery/local_storage_atom.cljs) | an atom backed by HTML 5 local storage
[`om-root`](src/cljs/com/palletops/bakery/om_root.cljs)     | an Om root element
[`sente`](src/clj/com/palletops/bakery/sente.clj)           | a sente clojure server
[`sente`](src/cljs/com/palletops/bakery/sente.cljs)         | a sente clojurescript client
[`secretary`](src/cljs/com/palletops/bakery/secretary.cljs) | secretary based routing


## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[leaven]:https://github.com/palletops/leaven "leaven component library"
