(ns ^:figwheel-always mw3.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [clojure.string :as string]))

(enable-console-print!)

;; The state of the application.
;; NOTE that app-state can contain vectors and maps only, not lists.
(defonce app-state (atom {:available-tabs {:#home-tab :#home-content
                                           :#params-tab :#params-content
                                           :#rules-tab :#rules-content
                                           :#world-tab :#world-content
                                           :#docs-tab :#docs-content}
                          :selected-tab :#home-tab
                          :text "Welcome to MicroWorld!"
                          :rules []
                          :params {:available-rulesets []
                                   :available-heightmaps []
                                   :ruleset ""
                                   :heightmap ""}
                          :world []}))

(deref app-state)
(:available-tabs (deref app-state))

(defn root-component [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil (dom/h1 nil (:text data))))))

(defn tab-bar [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:selected-tab :#home-tab
       :selected-content :#home-content})
    om/IRender
    (render [this]
            (dmu/ul nil
                    )))


(def pages [:#home-content :#params-content :#rules-content :#world-content :#docs-content])

(defn onclick-handler
  "Handles a click on the specified `tab` and reveals the specified `content`."
  [tab content]
  (doseq [page pages]
          (dommy/hide!(sel page)))
  (dommy/show! content))

;; NASTY! Javascript interprets hyphens as subtraction operators, so function names
;; exposed to raw Javascript (e.g. onclick event handlers) must not contain hyphens.

(defn ^:export home []
  (onclick-handler :#home-tab :#home-content))

(defn ^:export params []
  (onclick-handler :#params-tab :#params-content))


(om/root
 root-component
 app-state
 {:target (. js/document (getElementById "world-content"))})


