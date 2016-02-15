(ns ^:figwheel-always mw3.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [clojure.string :as string]))

(enable-console-print!)

;; The state of the application.
;; NOTE that app-state can contain vectors and maps only, not lists.
(defonce app-state (atom {;; Available tabs/pages within the application
                          :available-tabs {:#home-tab {:content :#home-content :text "Home"}
                                           :#params-tab {:content :#params-content :text "Parameters"}
                                           :#rules-tab {:content :#rules-content :text "Rules"}
                                           :#world-tab {:content :#world-content :text "World"}
                                           :#docs-tab {:content :#docs-content :text "Documentation"}}
                          ;; Parameters, set up on the parameters page.
                          :params {:available-rulesets []
                                   :available-heightmaps []
                                   :ruleset ""
                                   :heightmap ""}
                          ;; Currently active rules.
                          :rules []
                          ;; Currently selected tab.
                          :selected-tab :#home-tab
                          ;; Junk so I can see stuff during development.
                          :text "Welcome to MicroWorld!"
                          ;; The current state of the world.
                          :world []}))

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
;;     om/IWillMount
;;     (will-mount [_]
;;                 (let [selection (om/get-state owner :selected)]
;;                   (om/transact! data :selected-tab selection)))
    om/IRenderState
    (render-state [this state]
                  (apply dom/ul #js {:id "tab-bar"}
                         (om/build-all
                          (fn [key]
                            (reify
                              om/IRenderState
                              (render-state [this {:keys [selected]}]
                                            (dom/li
                                             #js {:onClick (fn [e] (put! selected @key))
                                                  :className (if (= key (:selected-tab data)) "selected" "tab")}
                                             (:text (key (:available-tabs data)))))))
                          (keys (:available-tabs data)){:init-state state})))))


(def pages [:#home-content :#params-content :#rules-content :#world-content :#docs-content])



(om/root
 root-component
 app-state
 {:target (. js/document (getElementById "world-content"))})

(om/root
 tab-bar
 app-state
 {:target (. js/document (getElementById "tab-bar-target"))})
