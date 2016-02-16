(ns mw3.rulesets)

;; Map of available rulesets. Key is a string name of the ruleset; value is a sequence of strings
;; of rule text.
(def rulesets
  {"ice-age" [
              "# Ice Age"
              "## Ruleset which attempts to model retreat of ice after an iceage"
              "### Vegetation rules"
              "if state is grassland then 1 chance in 10 state should be heath"
              "if state is heath and fertility is more than 10 and altitude is less than 120 then state should be scrub"
              "if state is scrub and fertility is more than 20 then 1 chance in 20 state should be forest"
              "if state is forest and fertility is more than 30 and altitude is less than 70 then state should be climax"
              "if state is climax then 1 chance in 500 state should be fire"
              ;; and so on. No need to fully populate this until something works
              ]
   "settlement" [
                 "# Settlement"
                 "## Ruleset which attempts to model human settlement in a landscape."
                 "if state is water then state should be water"
                 "if state is in grassland or heath and more than 3 neighbours are water and generation is more than 20 then state should be camp"
                 "if state is in grassland or heath and some neighbours are camp then 1 chance in 2 state should be pasture"
                 "if state is in grassland or heath and more than 2 neighbours are pasture then 1 chance in 3 state should be camp"
                 "if state is pasture and more than 3 neighbours are pasture and fewer than 1 neighbours are camp and fewer than 1 neighbours within 2 are house then state should be camp"
                 "if state is in grassland or heath and some neighbours within 2 are house then state should be pasture"
                 "if state is camp and some neighbours are ploughland then state should be camp"
                 "if state is pasture and fertility is less than 2 then 1 chance in 3 state should be heath"
                 "if state is camp then 1 chance in 5 state should be waste"
                 "if state is pasture and fewer than 1 neighbours within 3 are house and fewer than 1 neighbours within 2 are camp then state should be heath"
                 ;; and, again, so on.
                ]
   })
