(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `mwi-msme-eligible?` / `msme-set-aside-ineligible-claim?` are the SAME
  discipline applied to a genuinely Malawi-specific mechanism: the
  Public Procurement and Disposal of Public Assets Act, No. 7 of 2025,
  s.62(11) (own text, downloaded PDF, fetched directly 2026-07-23):
  'A procuring and disposing entity shall ensure that sixty per cent of
  the value of procurements carried out in a financial year under
  national competitive bidding is reserved for Malawian micro, small
  and medium sized enterprises and forty per cent is reserved for other
  bidders' -- together with the Public Procurement and Disposal of
  Public Assets (Participation by Micro, Small and Medium Enterprises)
  Order, 2020 (Government Notice 96 of 2020, malawilii.org, own PDF
  text downloaded directly), whose own Section 2 defines each size
  category as 'an enterprise comprising at least two of the following
  characteristics' across THREE independent axes -- annual turnover
  (Kwacha), employee headcount, and asset value (excluding land and
  buildings; the Order's own text scopes this exclusion 'in the case of
  a manufacturing enterprise', a genuinely ambiguous qualifier in the
  gazette text this catalog does NOT attempt to resolve -- it applies
  the stated asset-value band uniformly, an honestly-disclosed
  simplification of the Order's own ambiguous drafting, not a silent
  assumption).

  This is a GENUINELY DIFFERENT check SHAPE than every prior iso3166
  sibling this repo mirrors: GMB's GIEPA Special Investment Certificate
  is an ORIGIN-CONDITIONAL INVESTMENT-AMOUNT THRESHOLD (a single >=
  test whose own threshold value depends on the bidder's declared
  investor origin), LSO's Contractors Registration Certificate
  mechanism is a DISCRETE-CATEGORY -> FIXED-CONSTANT LOOKUP-TABLE
  EQUALITY check (compare a claimed fee for one declared category
  against ONE published constant), CAF's Marché réservé mechanism is a
  MULTI-CRITERION INCLUSION-ELIGIBILITY test over the bidder's own
  workforce composition/legal form, and Estonia's digital-signing-
  method check tests the VALIDITY OF THE FILING'S OWN EXECUTION
  INSTRUMENT (a procedural axis, not the bidder's business substance at
  all). Malawi's MSME set-aside mechanism is none of these: it is a
  MAJORITY-VOTE MULTI-AXIS BAND-MEMBERSHIP TEST -- the engagement
  declares its OWN size category (:micro/:small/:medium) AND three
  independent financial/operational facts about itself (turnover,
  employee count, assets), and eligibility requires AT LEAST TWO OF THE
  THREE declared facts to independently fall within THAT category's own
  published band. Unlike GMB's single-number threshold or LSO's
  single-lookup equality, no ONE fact is individually decisive here --
  a supplier with (say) small-band turnover but medium-band employee
  count and small-band assets is still eligible for :small (2 of 3
  match), while a supplier failing 2 of the 3 axes for its OWN declared
  category is ineligible even if the ONE axis it does match looks
  favourable. The first in this family to test 'a qualifying MAJORITY
  of several independent self-declared facts against a self-declared
  category's own band-set', rather than a single fact against a single
  threshold or lookup value.

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real procurement portal. It builds the RECORD an
  operator would keep, not the act of submitting a portal registration
  itself (that is `marketentry.operation`'s `:filing/submit`, always
  human-gated -- see README Actuation)."
  (:require [kotoba.lang.text :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(def ^:private money-scale
  "Sub-minor-unit scale used when comparing two money amounts: 1/10000 of
  a unit. Coarser than double representation error by many orders of
  magnitude, finer than any real currency's minor unit (2 decimals for
  most, 3 for KWD/BHD/OMR, 0 for JPY/KRW)."
  10000)

(defn- money=
  "Exact-at-money-precision equality for two amounts.

  `==` on raw doubles is NOT the right comparison for money. With
  whole-unit fees the two agree, but as soon as an amount carries
  cents the sum `base + rate x months` is routinely not the double
  nearest the true total, and a CORRECT claim compares false: measured
  on this exact shape, 40,989 of 327,060 cent-denominated combinations
  (12.5%) were rejected while being right, against 0 of 327,060 in
  whole units.

  Rounding both sides to `money-scale` before comparing removes the
  representation error while preserving every distinction money can
  actually carry."
  [x y]
  (and (number? x) (number? y)
       (= (Math/round (* money-scale (double x)))
          (Math/round (* money-scale (double y))))))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  ;; nil when any field is not a number: an un-recomputable engagement is
  ;; un-verifiable, which is neither `correct` nor a ClassCastException
  ;; thrown out of the caller.
  (when (and (number? base-fee) (number? monthly-rate) (number? monitoring-months))
    (+ (double base-fee)
       (* (double monthly-rate) (double monitoring-months)))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (money= claimed-fee (compute-engagement-fee engagement)))

(def msme-size-bands
  "Public Procurement and Disposal of Public Assets (Participation by
  Micro, Small and Medium Enterprises) Order, 2020 (Government Notice
  96 of 2020), own Section 2 definitions (own PDF text, malawilii.org,
  downloaded directly 2026-07-23): each size category is 'an enterprise
  comprising at least two of the following characteristics' across
  turnover (Kwacha, MWK), employee headcount, and asset value (Kwacha,
  MWK, excluding land and buildings -- see namespace docstring for the
  Order's own ambiguous 'in the case of a manufacturing enterprise'
  qualifier). Bands are contiguous and non-overlapping in the Order's
  own text (micro 'up to' 5,000,000 vs small 'above' 5,000,000, etc.)."
  {:micro  {:turnover-mwk [nil 5000000] :employees [nil 4] :assets-mwk [nil 1000000]}
   :small  {:turnover-mwk [5000000 50000000] :employees [4 20] :assets-mwk [1000000 20000000]}
   :medium {:turnover-mwk [50000000 500000000] :employees [20 99] :assets-mwk [20000000 250000000]}})

(defn- in-band?
  "Is `v` within the (lo excl, hi incl] band -- except the bottom band
  (`lo` nil), which is (_, hi incl]? One uniform convention across all
  three axes (turnover, employees, assets): each tier's `lo` is the
  tier immediately below's own `hi`, so 'above 20 but not more than 99'
  is expressed as `[20 99]` and tests `v > 20 and v <= 99`, matching
  the Order's own contiguous, non-overlapping band language exactly
  (for whole-number employee counts, `> 20` and `>= 21` coincide)."
  [[lo hi] v]
  (boolean
   (and (some? v)
        (<= (double v) (double hi))
        (or (nil? lo) (> (double v) (double lo))))))

(defn msme-criteria-met-count
  "How many of the THREE size-band criteria (turnover / employees /
  assets) does `engagement`'s own declared facts satisfy for
  `category`? An unrecognized category always scores 0 (fails closed)."
  [category {:keys [annual-turnover-mwk employee-count assets-mwk]}]
  (let [band (get msme-size-bands category)]
    (if (nil? band)
      0
      (let [band-turnover (:turnover-mwk band)
            band-employees (:employees band)
            band-assets (:assets-mwk band)]
        (cond-> 0
          (in-band? band-turnover annual-turnover-mwk) inc
          (in-band? band-employees employee-count) inc
          (in-band? band-assets assets-mwk) inc)))))

(defn mwi-msme-eligible?
  "The ground-truth MSME set-aside eligibility for `engagement`,
  independently recomputed: does it satisfy AT LEAST TWO of the THREE
  size-band criteria for its OWN declared `:msme-declared-category`
  (:micro/:small/:medium)? A missing/unrecognized category or missing
  facts simply fails (does not throw)."
  [{:keys [msme-declared-category] :as engagement}]
  (>= (msme-criteria-met-count msme-declared-category engagement) 2))

(defn msme-set-aside-ineligible-claim?
  "Does `engagement` declare `:seeking-msme-set-aside? true` (i.e. it is
  claiming eligibility for the Act's own s.62(11) 60%/40%
  national-competitive-bidding MSME reservation) while the
  INDEPENDENTLY recomputed `mwi-msme-eligible?` is false? An engagement
  not seeking the set-aside is never flagged by this check
  (entity/engagement-scope-gated, the same discipline GMB's
  `:seeking-sic?`-gated SIC check uses)."
  [{:keys [seeking-msme-set-aside?] :as engagement}]
  (boolean (and seeking-msme-set-aside? (not (mwi-msme-eligible? engagement)))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a portal registration
  package. Pure function -- does not touch any real procurement
  portal."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a portal
  registration (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
