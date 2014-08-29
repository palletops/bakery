# example.webapp

A UI for [pallet](http://palletops.com).

## Usage

### Repl
Start a repl, and run

```clj
(run)
(browser-repl)
```

Point your browser at http://localhost:3000

### Uberjar

```clj
lein uberjar
java -jar target/example.webapp-*-standalone.jar
```

## Services

Services are discovered using `example.webapp.services.*` namespaces.


## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
