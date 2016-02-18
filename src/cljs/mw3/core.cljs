(ns ^:figwheel-always mw3.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [mw3.rulesets :as rulesets]
    [dommy.core :as dommy :refer-macros [sel sel1]]
    [dommy.template :as temp]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Data declarations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Tiles supplied in the standard distribution. Obviously this isn't all the states you can have.
(def tiles ["abandoned"
            "black"
            "camp"
            "climax"
            "crop"
            "error"
            "fire"
            "forest"
            "grassland"
            "heath"
            "house"
            "ice"
            "inn"
            "market"
            "meadow"
            "pasture"
            "plague"
            "ploughland"
            "scrub"
            "snow"
            "waste"
            "water"
            "white"])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Navigation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Tabs in the single-page app.
(def available-tabs {:#home-tab {:content :#home-content :text "Home"}
                     :#params-tab {:content :#params-content :text "Parameters"}
                     :#rules-tab {:content :#rules-content :text "Rules"}
                     :#world-tab {:content :#world-content :text "World"}
                     :#docs-tab {:content :#docs-content :text "Documentation"}})

;; page-to-page navigation
(defn tab-handler
  "Handle a click event on the tab with id `tab-id`"
  [e tab-id]
  (.log js/console (str "You clicked '" tab-id "'"))
  (doseq [key (keys available-tabs)]
    (let [content-id (:content (available-tabs key))
          display (if (= key tab-id) "block" "none")]
        (dommy/set-style! (sel1 content-id) :display display))))

(defn tab-click-listener
  "Set up a click listener on the tab with this `tab-id`"
  [tab-id]
  (dommy/set-text! (sel1 tab-id) (:text (tab-id available-tabs)))
  (dommy/listen! (sel1 tab-id) :click (fn [e] (tab-handler e tab-id))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Rules page
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn load-ruleset
  [name]
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Set up the screen on loading
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; listeners for the tab bar
(tab-click-listener :#home-tab)
(tab-click-listener :#params-tab)
(tab-click-listener :#rules-tab)
(tab-click-listener :#world-tab)
(tab-click-listener :#docs-tab)

;; hide controls which aren't needed if we're doing things client side
(doseq [to-hide (sel :.hide-if-active)]
  (dommy/set-style! to-hide :display "none"))

;; hide all pages except home-tab
(tab-handler nil :#home-tab)

;; put the default ruleset into the rulesets pages
(dommy/set-text! (sel1 :#rules-src) (rulesets/ruleset-as-single-string "ice-age"))
