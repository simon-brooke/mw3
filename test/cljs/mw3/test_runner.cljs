(ns mw3.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [mw3.core-test]))

(enable-console-print!)

(doo-tests 'mw3.core-test)
