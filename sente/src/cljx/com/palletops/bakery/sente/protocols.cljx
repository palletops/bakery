(ns com.palletops.bakery.sente.protocols)

(defprotocol HandlerReturnable
  (handler [component]
    "Return a sente event handler function based on the component."))
