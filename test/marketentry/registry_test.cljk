(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "MWI" 0)
        s (registry/register-submit "eng-1" "MWI" 0)]
    (is (= "MWI-DFT-000000" (get d "draft_number")))
    (is (= "MWI-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "MWI" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest msme-eligible-micro-band
  (testing "a micro-declared enterprise scoring 2 of 3 micro criteria is eligible"
    (is (true? (registry/mwi-msme-eligible?
                {:msme-declared-category :micro
                 :annual-turnover-mwk 4000000 :employee-count 3 :assets-mwk 900000})))
    (is (= 3 (registry/msme-criteria-met-count
              :micro {:annual-turnover-mwk 4000000 :employee-count 3 :assets-mwk 900000})))))

(deftest msme-eligible-requires-two-of-three
  (testing "matching only 1 of 3 criteria for the declared category is ineligible"
    (is (= 1 (registry/msme-criteria-met-count
              :small {:annual-turnover-mwk 60000000 :employee-count 8 :assets-mwk 30000000})))
    (is (false? (registry/mwi-msme-eligible?
                 {:msme-declared-category :small
                  :annual-turnover-mwk 60000000 :employee-count 8 :assets-mwk 30000000}))))
  (testing "matching exactly 2 of 3 criteria for the declared category is eligible"
    (is (= 2 (registry/msme-criteria-met-count
              :small {:annual-turnover-mwk 60000000 :employee-count 8 :assets-mwk 5000000})))
    (is (true? (registry/mwi-msme-eligible?
                {:msme-declared-category :small
                 :annual-turnover-mwk 60000000 :employee-count 8 :assets-mwk 5000000})))))

(deftest msme-eligible-band-boundaries
  (testing "the micro/small boundary at exactly 5,000,000 turnover belongs to micro (up to), not small (above)"
    (is (= 1 (registry/msme-criteria-met-count :micro {:annual-turnover-mwk 5000000})))
    (is (= 0 (registry/msme-criteria-met-count :small {:annual-turnover-mwk 5000000}))))
  (testing "employee-count bands are inclusive on both ends of the stated range"
    (is (= 1 (registry/msme-criteria-met-count :small {:employee-count 5})))
    (is (= 1 (registry/msme-criteria-met-count :small {:employee-count 20})))
    (is (= 0 (registry/msme-criteria-met-count :small {:employee-count 21})))))

(deftest msme-eligible-missing-or-unrecognized-category-fails-closed
  (is (false? (registry/mwi-msme-eligible? {:annual-turnover-mwk 1000000 :employee-count 2 :assets-mwk 500000})))
  (is (false? (registry/mwi-msme-eligible? {:msme-declared-category :giant :annual-turnover-mwk 1000000})))
  (is (= 0 (registry/msme-criteria-met-count :giant {:annual-turnover-mwk 1000000}))))

(deftest msme-set-aside-ineligible-claim-is-entity-scope-gated
  (testing "an engagement NOT declared :seeking-msme-set-aside? is never flagged, even if it would fail eligibility"
    (is (false? (registry/msme-set-aside-ineligible-claim?
                 {:seeking-msme-set-aside? false :msme-declared-category :small
                  :annual-turnover-mwk 60000000 :employee-count 8 :assets-mwk 30000000}))))
  (testing "a set-aside-seeking engagement that fails its own declared category's 2-of-3 test -> ineligible claim"
    (is (true? (registry/msme-set-aside-ineligible-claim?
                {:seeking-msme-set-aside? true :msme-declared-category :small
                 :annual-turnover-mwk 60000000 :employee-count 8 :assets-mwk 30000000}))))
  (testing "a set-aside-seeking engagement that DOES satisfy its own declared category's 2-of-3 test -> not flagged"
    (is (false? (registry/msme-set-aside-ineligible-claim?
                 {:seeking-msme-set-aside? true :msme-declared-category :micro
                  :annual-turnover-mwk 4000000 :employee-count 3 :assets-mwk 900000})))))

;; ---------------------------------------------------------------------------
;; Money is compared at money precision, not at double precision
;; ---------------------------------------------------------------------------

(deftest whole-unit-fees-were-already-correct-and-stay-correct
  (testing "the seeded shape: base + rate x months in whole currency units"
    (is (registry/engagement-fee-matches-claim?
         {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12
           :claimed-fee 860000.0}))))

(deftest cent-denominated-fees-are-no-longer-rejected-while-correct
  (testing "`(== (double claimed) (+ (double base) (* (double rate) (double months))))`
            rejected CORRECT totals once an amount carried cents -- 40,989 of
            327,060 combinations (12.5%), against 0 of 327,060 in whole units"
    (let [bad (for [m (range 1 37)
                    bc (range 10000 90000 2100)
                    rc (range 500 6000 210)
                    :let [truth (/ (+ bc (* rc m)) 100.0)]
                    :when (not (registry/engagement-fee-matches-claim?
                                {:base-fee (/ bc 100.0) :monthly-rate (/ rc 100.0)
                                  :monitoring-months m :claimed-fee truth}))]
                [m (/ bc 100.0) (/ rc 100.0) truth])]
      (is (empty? bad) (str "false rejections: " (count bad) " e.g. " (first bad))))))

(deftest a-genuinely-wrong-fee-is-still-caught
  (testing "rounding to money precision must not blunt the check"
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12
                :claimed-fee 860000.01})))
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12
                :claimed-fee 859999.99})))))

(deftest an-unverifiable-fee-never-matches
  (testing "un-verifiable is not the same as correct, and not a crash"
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12})))
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee "500000" :monthly-rate 30000 :monitoring-months 12
                :claimed-fee 860000.0})))
    (is (nil? (registry/compute-engagement-fee {:base-fee 500000 :monthly-rate 30000})))))
