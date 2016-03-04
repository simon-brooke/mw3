(ns ^:figwheel-always mw3.core
  (:use-macros [dommy.template :only [node deftemplate]])
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
;; Params page
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn rebuild-ruleset-menu
  []
  (let [menu (sel1 :#params-ruleset)]
    (dommy/set-html!
      menu
      (temp/->document-fragment (temp/node [:ul "Froboz"])))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Rules page
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftemplate rule-editor
  ;; "Constructs an editor for this `rule` with this `index`, given this `total`
  ;; number of rules.
  [rule index total]
  [:div
   {:id (str "rule-editor-" index) :class "rule-editor"}
   [:input {:type "text" :id (str "rule-input-" index) :class "rule-input" :value rule}]
   [:div {:id (str "rule-controls-" index) :class "rule-controls"}
    [:input {:type "button"
             :id (str "rule-ok-" index)
             :class "rule-ok"
             :value "ok"}]                             ;; &#x2714;
    [:input {:type "button"
             :id (str "rule-up-" index)
             :class "rule-up"
             :value "up"
             :disabled (= index 0)}]                   ;; &uarr;
    [:input {:type "button"
             :id (str "rule-down-" index)
             :class "rule-down"
             :value "down"
             :disabled (= index total)}]               ;; &darr;
    [:input {:type "button"
             :id (str "rule-delete-" index)
             :class "rule-delete"
             :value "delete"}]]                        ;; &#x2718;
   [:pre {:id (str "rule-feedback-" index) :class "rule-feedback"}]])

(defn rule-up-handler
  "A handler to move the rule with index `n` one place up the list."
  [n id]
  (.log js/console (str id " pressed")))

(defn rule-down-handler
  "A handler to move the rule with index `n` one place down the list."
  [n id]
  (.log js/console (str id " pressed")))

(defn rule-compile-handler
  "A handler to compile the rule with index `n`."
  [n id]
  (.log js/console (str id " pressed")))

(defn rule-delete-handler
  "A handler to delete the rule with index `n`."
  [n id]
  (.log js/console (str id " pressed")))

(defn load-ruleset
  "Loads the ruleset with the specified `name` into a set of rule editors."
  [name]
  (let [rules-container (sel1 :#rules-container)
        ruleset (rulesets/rulesets name)
        total (count ruleset)
        indexed-rules (map #(list %1 %2) ruleset (range))]
    (dommy/clear! rules-container)
    (doseq [[rule index] indexed-rules]
      (dommy/append! rules-container (rule-editor rule index total)))
    (doseq [[rule index] indexed-rules]
      (let [ok-id (str "rule-ok-" index)
            up-id (str "rule-up-" index)
            down-id (str "rule-down-" index)
            delete-id (str "rule-delete-" index)
            ok-elt (sel1 ok-id)
            up-elt (sel1 up-id)
            down-elt (sel1 down-id)
            delete-elt (sel1 delete-id)]
        (if ok-elt
          (dommy/listen! (sel1 ok-id) :click (fn [e] (rule-compile-handler e ok-id)))
          (.log js/console (str "Could not find an element with id " ok-id)))
        (if up-elt
          (dommy/listen! (sel1 up-id) :click (fn [e] (rule-up-handler e up-id))))
        (if down-elt
          (dommy/listen! (sel1 down-id) :click (fn [e] (rule-down-handler e down-id))))
        (if delete-elt
          (dommy/listen! (sel1 delete-id) :click (fn [e] (rule-delete-handler e delete-id))))))))

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

;; set up the rulesets menu with the rulesets we actually have.
;; (rebuild-ruleset-menu)

;; put the default ruleset into the rulesets pages
(load-ruleset "settlement")

