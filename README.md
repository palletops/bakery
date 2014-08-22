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
`om-root` | an Om root element
`jetty`   | a jetty server
`httpkit` | a httpkit server


## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[leaven]:https://github.com/palletops/leaven "leaven component library"
