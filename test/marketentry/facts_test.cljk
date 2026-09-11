(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest mwi-has-spec-basis
  (let [sb (facts/spec-basis "MWI")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/corporate-number-spec-basis "MWI")))
    (is (some? (facts/msme-spec-basis "MWI")))))

(deftest mwi-rep-spec-basis-is-honestly-absent
  (testing "PPDA's own Section 79 debarment grounds attach to the bidder entity, not personally to its directors/representatives -- deliberately not claimed"
    (is (nil? (facts/rep-spec-basis "MWI")))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "MWI")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "MWI" all)))
    (is (not (facts/required-evidence-satisfied? "MWI" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["MWI" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))

(deftest msme-spec-basis-criteria
  (let [msme (facts/msme-spec-basis "MWI")]
    (is (= 5000000 (get-in msme [:msme-criteria :micro :turnover-mwk-max])))
    (is (= 4 (get-in msme [:msme-criteria :micro :employees-max])))
    (is (= 20 (get-in msme [:msme-criteria :small :employees-max])))
    (is (= 99 (get-in msme [:msme-criteria :medium :employees-max])))
    (is (= 250000000 (get-in msme [:msme-criteria :medium :assets-mwk-max])))))
