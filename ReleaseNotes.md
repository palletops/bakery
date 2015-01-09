## 0.3.0

- Update leaven to 0.3.0

- Update secretary to 1.2.1

- Update weasel to 0.4.2

- Update example webapp to lein-cljsbuild 1.0.4

- Allow om-root options from a component
  Allow a component to contribute to the options in the om root (eg. to
  passed :shared values).

- Fix config handling in weasel component

- Add post-condtion to httpkit start

- Simplify sente component, separate router
  Removes the router to a separate component to allow usage with a custom
  router.

  Requires the handler for the router to be a component, in order that it
  may have it's own life-cycle.  The handler component must implement the
  HandlerReturnable protocol.

- Add core-async-bridge component
  Adds a leaven component to send values from a core.async channel via
  sente.

- Add a core.async channel component
  Adds a leaven component for a core.async channel that can be started and
  stopped.

## 0.2.0

- Update to use leaven Startable and Stoppable
  Updates to use the new protocols in leaven 0.2.0.

## 0.1.2

- Pass default atom to local-storage-atom
  Allow use of local-storage-atom with non-core atom implementations, like
  reagent's atom.  This is a breaking change that changes the expected
  argument type from a default map to an atom o fthe required type
  containing the default values.

## 0.1.1

- Update to sente 1.0.0

## 0.1.0

- Initial release
