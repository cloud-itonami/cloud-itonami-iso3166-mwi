(ns statute.facts-test
  (:require [kotoba.lang.text :as str]
            [clojure.test :refer [deftest is]]
            [statute.facts :as facts]))

(deftest mwi-has-spec-basis
  (let [sb (facts/spec-basis "MWI")]
    (is (= 3 (count sb)))
    (is (every? #(str/starts-with? (:statute/url %) "https://") sb))
    (is (every? :statute/law-number sb))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["MWI" "JPN" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ATL" "JPN"] (:missing-jurisdictions c)))))

(deftest by-topic-filters
  (is (= ["mwi.employment-act"]
         (mapv :statute/id (facts/by-topic "MWI" :labor))))
  (is (= ["mwi.value-added-tax-act"]
         (mapv :statute/id (facts/by-topic "MWI" :tax))))
  (is (= ["mwi.companies-act-2013"]
         (mapv :statute/id (facts/by-topic "MWI" :corporate-governance))))
  (is (empty? (facts/by-topic "ATL" :labor))))
