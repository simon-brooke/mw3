(ns ^:figwheel-always mw3.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [clojure.string :as string]
            [secretary.core :as sec :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [dommy.core :as dommy :refer-macros [sel sel1]])
  (:import goog.History))

(def pages [:#home-content :#params-content :#rules-content :#world-content :#docs-content])

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
  (dommy/listen! (sel1 tab-id) :click (fn [e] (tab-handler e tab-id))))

;; listeners for the tab bar
(tab-click-listener :#home-tab)
(tab-click-listener :#params-tab)
(tab-click-listener :#rules-tab)
(tab-click-listener :#world-tab)
(tab-click-listener :#docs-tab)
